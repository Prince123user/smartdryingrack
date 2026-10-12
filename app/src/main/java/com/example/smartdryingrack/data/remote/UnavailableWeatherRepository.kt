package com.example.smartdryingrack.data.remote

import com.example.smartdryingrack.domain.model.WeatherData
import com.example.smartdryingrack.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** No configured weather provider. Missing forecasts must never drive motor commands. */
@Singleton
class UnavailableWeatherRepository @Inject constructor() : WeatherRepository {
    override val forecast = MutableStateFlow<WeatherData?>(null).asStateFlow()
}
