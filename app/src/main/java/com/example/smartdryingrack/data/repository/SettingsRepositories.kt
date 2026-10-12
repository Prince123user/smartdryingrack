package com.example.smartdryingrack.data.repository

import android.content.Context
import com.example.smartdryingrack.domain.model.AutomationSettings
import com.example.smartdryingrack.domain.model.RackSchedule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("rack_settings", Context.MODE_PRIVATE).apply {
        // Adopt the newly requested dark reference once; subsequent appearance choices persist.
        if (!getBoolean("neon_theme_initialized", false)) {
            edit().putBoolean("darkTheme", true).putBoolean("neon_theme_initialized", true).apply()
        }
    }
    private val _settings = MutableStateFlow(AutomationSettings(
        rainSensitivity = prefs.getFloat("rainSensitivity", 0.4f),
        dryThresholdPercent = prefs.getInt("dryThreshold", 90),
        useWeather = prefs.getBoolean("useWeather", false),
        clothesDryAlert = prefs.getBoolean("clothesDryAlert", true),
        darkTheme = prefs.getBoolean("darkTheme", true),
        deviceName = prefs.getString("deviceName", "My drying rack") ?: "My drying rack"
    ))
    val settings = _settings.asStateFlow()
    @Synchronized
    fun update(transform: (AutomationSettings) -> AutomationSettings) {
        val next = transform(_settings.value)
        require(next.rainSensitivity.isFinite() && next.rainSensitivity in 0f..1f)
        require(next.dryThresholdPercent in 50..100)
        prefs.edit().putFloat("rainSensitivity", next.rainSensitivity)
            .putInt("dryThreshold", next.dryThresholdPercent).putBoolean("useWeather", next.useWeather)
            .putBoolean("clothesDryAlert", next.clothesDryAlert).putBoolean("darkTheme", next.darkTheme)
            .putString("deviceName", next.deviceName).apply()
        _settings.value = next
    }
}

fun encodeSchedule(schedule: RackSchedule): String = JSONObject().apply {
    put("enabled", schedule.enabled)
    put("extend", schedule.extendTime.toString())
    put("retract", schedule.retractTime.toString())
    java.time.DayOfWeek.entries.forEach { put(it.name, schedule.isDayEnabled(it)) }
}.toString()

fun decodeSchedule(value: String): RackSchedule {
    val json = JSONObject(value)
    var result = RackSchedule(enabled = json.getBoolean("enabled"),
        extendTime = LocalTime.parse(json.getString("extend")), retractTime = LocalTime.parse(json.getString("retract")))
    java.time.DayOfWeek.entries.forEach { result = result.withDay(it, json.getBoolean(it.name)) }
    return result
}

@Singleton
class ScheduleRepository @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("rack_schedule", Context.MODE_PRIVATE)
    private val _schedule = MutableStateFlow(runCatching {
        prefs.getString("schedule", null)?.let(::decodeSchedule) ?: RackSchedule(enabled = false)
    }.getOrDefault(RackSchedule(enabled = false)))
    val schedule = _schedule.asStateFlow()
    @Synchronized
    fun update(transform: (RackSchedule) -> RackSchedule) {
        val next = transform(_schedule.value)
        prefs.edit().putString("schedule", encodeSchedule(next)).apply()
        _schedule.value = next
    }
}
