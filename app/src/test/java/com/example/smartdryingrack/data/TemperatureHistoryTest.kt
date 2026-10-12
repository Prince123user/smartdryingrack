package com.example.smartdryingrack.data

import android.content.Context
import android.content.SharedPreferences
import com.example.smartdryingrack.data.repository.*
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class TemperatureHistoryTest {
    @Test fun historyRoundTripSortsRealMeasurements() {
        val samples = listOf(TemperaturePoint(2000, 32.5f), TemperaturePoint(1000, 29f))
        assertEquals(samples.sortedBy { it.timestamp }, decodeTemperatureHistory(encodeTemperatureHistory(samples)))
        assertTrue(decodeTemperatureHistory("[]").isEmpty())
    }

    @Test fun recordingRejectsInvalidAndOverFrequentSamplesAndPersistsTheNextSample() {
        val context = mock(Context::class.java)
        val prefs = mock(SharedPreferences::class.java)
        val editor = mock(SharedPreferences.Editor::class.java, RETURNS_SELF)
        `when`(context.getSharedPreferences("rack_temperature", Context.MODE_PRIVATE)).thenReturn(prefs)
        `when`(prefs.getString("points", "[]")).thenReturn(encodeTemperatureHistory(listOf(TemperaturePoint(1000, 28f))))
        `when`(prefs.edit()).thenReturn(editor)
        val history = TemperatureHistory(context)
        history.record(2000, 29f)
        history.record(301000, Float.NaN)
        history.record(301000, 999f)
        verify(prefs, never()).edit()
        history.record(301000, 30f)
        val encoded = ArgumentCaptor.forClass(String::class.java)
        verify(editor).putString(eq("points"), encoded.capture())
        assertEquals(listOf(TemperaturePoint(1000, 28f), TemperaturePoint(301000, 30f)), decodeTemperatureHistory(encoded.value))
    }
}
