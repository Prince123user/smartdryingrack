package com.example.smartdryingrack.domain.repository

import com.example.smartdryingrack.domain.model.WeatherData
import kotlinx.coroutines.flow.StateFlow

/** Swap the implementation (OpenWeatherMap, etc.) without touching UI or automation code. */
interface WeatherRepository {
    val forecast: StateFlow<WeatherData?>
}
