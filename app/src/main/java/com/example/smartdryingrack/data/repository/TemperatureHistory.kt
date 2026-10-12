package com.example.smartdryingrack.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

data class TemperaturePoint(val timestamp: Long, val celsius: Float)

fun encodeTemperatureHistory(points: List<TemperaturePoint>): String = JSONArray().apply {
    points.forEach { put(JSONObject().put("timestamp", it.timestamp).put("celsius", it.celsius.toDouble())) }
}.toString()

fun decodeTemperatureHistory(value: String): List<TemperaturePoint> {
    val array = JSONArray(value)
    return (0 until array.length()).map {
        val point = array.getJSONObject(it)
        TemperaturePoint(point.getLong("timestamp"), point.getDouble("celsius").toFloat())
    }.filter { it.timestamp > 0 && it.celsius.isFinite() && it.celsius in -50f..80f }.sortedBy { it.timestamp }.takeLast(2016)
}

@Singleton
class TemperatureHistory @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("rack_temperature", Context.MODE_PRIVATE)
    private val _points = MutableStateFlow(runCatching { decodeTemperatureHistory(prefs.getString("points", "[]") ?: "[]") }.getOrDefault(emptyList()))
    val points = _points.asStateFlow()

    @Synchronized fun record(timestamp: Long, celsius: Float) {
        if (!celsius.isFinite() || celsius !in -50f..80f || timestamp <= 0) return
        val latest = _points.value.lastOrNull()
        if (latest != null && timestamp - latest.timestamp < 5 * 60 * 1000L) return
        val cutoff = timestamp - 7 * 24 * 60 * 60 * 1000L
        val updated = (_points.value.filter { it.timestamp >= cutoff } + TemperaturePoint(timestamp, celsius)).takeLast(2016)
        prefs.edit().putString("points", encodeTemperatureHistory(updated)).apply()
        _points.value = updated
    }
}
