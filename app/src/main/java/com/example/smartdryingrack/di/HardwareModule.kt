package com.example.smartdryingrack.di

import com.example.smartdryingrack.data.hardware.RackController
import com.example.smartdryingrack.data.hardware.rest.WifiRackController
import com.example.smartdryingrack.data.remote.UnavailableWeatherRepository
import com.example.smartdryingrack.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {
    @Binds abstract fun bindController(impl: WifiRackController): RackController
    @Binds abstract fun bindWeather(impl: UnavailableWeatherRepository): WeatherRepository
}
