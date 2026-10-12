package com.example.smartdryingrack.data.hardware.mock

import com.example.smartdryingrack.data.hardware.DeviceSimulator
import com.example.smartdryingrack.data.hardware.RackController
import com.example.smartdryingrack.domain.model.ConnectionState
import com.example.smartdryingrack.domain.model.RackState
import com.example.smartdryingrack.domain.model.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockRackController @Inject constructor() : RackController, DeviceSimulator {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val rackState = MutableStateFlow(RackState.EXTENDED)
    private val connection = MutableStateFlow(ConnectionState.OFFLINE)
    private val sensor = MutableStateFlow(
        SensorData(31f, 68f, rainDetected = false, clothesDryPercentage = 62, timestamp = System.currentTimeMillis())
    )

    @Volatile private var rain = false
    @Volatile private var sensorError = false
    @Volatile private var dryPercent = 62
    private var moveJob: Job? = null
    private var tickJob: Job? = null

    override fun observeRackState(): Flow<RackState> = rackState.asStateFlow()
    override fun observeSensorData(): Flow<SensorData> = sensor.asStateFlow()
    override fun observeConnection(): Flow<ConnectionState> = connection.asStateFlow()

    override suspend fun connect() {
        if (connection.value == ConnectionState.ONLINE) return
        connection.value = ConnectionState.CONNECTING
        delay(800)
        connection.value = ConnectionState.ONLINE
        startTicking()
    }

    override suspend fun disconnect() {
        tickJob?.cancel()
        tickJob = null
        connection.value = ConnectionState.OFFLINE
    }

    override suspend fun isConnected(): Boolean = connection.value == ConnectionState.ONLINE

    override suspend fun extend() = move(RackState.EXTENDING, RackState.EXTENDED)

    override suspend fun retract() = move(RackState.RETRACTING, RackState.RETRACTED)

    override suspend fun stop() {
        requireOnline()
        moveJob?.cancel()
        val s = rackState.value
        if (s == RackState.EXTENDING || s == RackState.RETRACTING) rackState.value = RackState.STOPPED
    }

    private fun move(during: RackState, done: RackState) {
        requireOnline()
        moveJob?.cancel()
        moveJob = scope.launch {
            rackState.value = during
            delay(TRAVEL_MS)
            rackState.value = done
        }
    }

    private fun requireOnline() {
        check(connection.value == ConnectionState.ONLINE) { "Rack is offline" }
    }

    private fun startTicking() {
        if (tickJob?.isActive == true) return
        tickJob = scope.launch {
            var tick = 0
            while (true) {
                delay(2_000)
                tick++
                if (rackState.value == RackState.EXTENDED && !rain && dryPercent < 100 && tick % 5 == 0) dryPercent++
                val temp = 31f + Random.nextFloat() - 0.5f
                val humidity = if (rain) 95f else 68f + Random.nextFloat() * 2f
                sensor.value = SensorData(
                    temperature = if (sensorError) 999f else temp,
                    humidity = humidity,
                    rainDetected = rain,
                    clothesDryPercentage = dryPercent,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    override fun setRain(on: Boolean) { rain = on }
    override fun setClothesDry(percent: Int) { dryPercent = percent.coerceIn(0, 100) }
    override fun setSensorError(on: Boolean) { sensorError = on }
    override suspend fun setOffline(offline: Boolean) { if (offline) disconnect() else connect() }

    private companion object {
        const val TRAVEL_MS = 3_000L
    }
}
