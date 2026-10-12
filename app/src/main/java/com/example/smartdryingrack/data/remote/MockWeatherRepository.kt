package com.example.smartdryingrack.data.remote

import com.example.smartdryingrack.domain.model.WeatherData
import com.example.smartdryingrack.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/** Demo weather. Replaced by a Retrofit-backed repository in the weather phase. */
@Singleton
class MockWeatherRepository @Inject constructor() : WeatherRepository {
    private val fourPm = LocalDate.now().atTime(16, 0)
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    private val _forecast = MutableStateFlow<WeatherData?>(
        WeatherData(
            temperature = 31.0,
            humidity = 68,
            rainProbability = 60,
            condition = "Clear skies",
            windSpeed = 8.0,
            forecastTime = fourPm,
            isSimulated = true
        )
    )
    override val forecast: StateFlow<WeatherData?> = _forecast
}
