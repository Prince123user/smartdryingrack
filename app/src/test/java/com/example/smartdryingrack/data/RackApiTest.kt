package com.example.smartdryingrack.data

import com.example.smartdryingrack.data.hardware.rest.*
import com.example.smartdryingrack.domain.model.RackState
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.net.InetAddress
import java.net.URL
import java.net.HttpURLConnection

class RackApiTest {
    private val status = """{"state":"RETRACTED","firmwareVersion":"1.2.0","sensors":{"temperature":29.5,"humidity":64,"rainDetected":false,"clothesDryPercentage":null}}"""

    @Test fun onlyPrivateNetworksAllowPlainHttp() {
        assertEquals("http://192.168.1.50", normalizeRackUrl(" http://192.168.1.50/ "))
        assertEquals("https://rack.example.com:8443", normalizeRackUrl("https://rack.example.com:8443"))
        listOf("http://example.com", "http://127.0.0.1", "http://172.32.0.1", "http://192.168.1.999", "file:///etc/passwd",
            "http://user:pass@192.168.1.1", "http://192.168.1.1/api/status", "http://192.168.1.1?key=secret").forEach {
            assertThrows(IllegalArgumentException::class.java) { normalizeRackUrl(it) }
        }
    }

    @Test fun noDrynessSensorRemainsUnavailable() {
        val parsed = parseRackSnapshot(status, 123L)
        assertEquals(RackState.RETRACTED, parsed.state)
        assertEquals("1.2.0", parsed.firmware)
        assertNull(parsed.sensors.clothesDryPercentage)
        assertEquals(123L, parsed.sensors.timestamp)
        assertTrue(parsed.sensors.isValid)
    }

    @Test fun malformedAndOutOfRangeTelemetryIsRejected() {
        assertThrows(Exception::class.java) { parseRackSnapshot("{}", 0L) }
        assertThrows(Exception::class.java) { parseRackSnapshot(status.replace("RETRACTED", "made-up"), 0L) }
        assertThrows(IllegalArgumentException::class.java) { parseRackSnapshot(status.replace("29.5", "999"), 0L) }
        assertThrows(IllegalArgumentException::class.java) { parseRackSnapshot(status.replace("\"humidity\":64", "\"humidity\":-1"), 0L) }
    }

    @Test fun fractionalDrynessAndStringSensorReadingsAreRejected() {
        assertThrows(IllegalArgumentException::class.java) { parseRackSnapshot(status.replace("\"clothesDryPercentage\":null", "\"clothesDryPercentage\":52.5"), 0L) }
        assertThrows(IllegalArgumentException::class.java) { parseRackSnapshot(status.replace("29.5", "\"29.5\""), 0L) }
    }

    // Keep HTTP inside the machine: private-interface traffic is routed through cloud egress.
    // Address validation still receives a private rack URL; only the test connection factory maps it to loopback.
    private fun api() = RackApi { url ->
        URL("http://127.0.0.1:${url.port}${url.path}").openConnection() as HttpURLConnection
    }
    private fun withServer(run: (MockWebServer, String) -> Unit) {
        val server = MockWebServer()
        server.start(InetAddress.getByName("127.0.0.1"), 0)
        try { run(server, "http://192.168.1.50:${server.port}") } finally { server.shutdown() }
    }

    @Test fun readsTelemetryAndSendsAcknowledgedCommandsOverHttp() = withServer { server, url ->
        server.enqueue(MockResponse().setBody(status))
        server.enqueue(MockResponse().setResponseCode(202).setBody("""{"accepted":true}"""))
        val api = api()
        assertEquals(RackState.RETRACTED, api.status(url).state)
        api.command(url, "EXTEND")
        assertEquals("/api/status", server.takeRequest().path)
        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/commands", request.path)
        val command = JSONObject(request.body.readUtf8())
        assertEquals("EXTEND", command.getString("action"))
        assertTrue(command.getString("requestId").isNotBlank())
    }

    @Test fun rejectionDoesNotRetryMotorCommand() = withServer { server, url ->
        server.enqueue(MockResponse().setResponseCode(503))
        val error = assertThrows(java.io.IOException::class.java) { api().command(url, "RETRACT") }
        assertEquals(url + "\n" + error.stackTraceToString(), 1, server.requestCount)
    }

    @Test fun redirectedStatusCannotMarkDeviceOnline() = withServer { server, url ->
        server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", "https://example.com"))
        assertThrows(java.io.IOException::class.java) { api().status(url) }
        assertEquals(1, server.requestCount)
    }
}
