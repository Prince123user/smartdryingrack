package com.example.smartdryingrack.di

import com.example.smartdryingrack.data.hardware.DeviceSimulator
import com.example.smartdryingrack.data.hardware.RackController
import com.example.smartdryingrack.data.hardware.mock.MockRackController
import com.example.smartdryingrack.data.remote.MockWeatherRepository
import com.example.smartdryingrack.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {
    @Binds abstract fun bindController(impl: MockRackController): RackController
    @Binds abstract fun bindSimulator(impl: MockRackController): DeviceSimulator
    @Binds abstract fun bindWeather(impl: MockWeatherRepository): WeatherRepository
}
