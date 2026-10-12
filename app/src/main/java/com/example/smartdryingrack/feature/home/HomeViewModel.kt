package com.example.smartdryingrack.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdryingrack.data.repository.RackRepository
import com.example.smartdryingrack.data.repository.SettingsRepository
import com.example.smartdryingrack.domain.model.ConnectionState
import com.example.smartdryingrack.domain.model.RackMode
import com.example.smartdryingrack.domain.model.RackState
import com.example.smartdryingrack.domain.model.SensorData
import com.example.smartdryingrack.domain.model.WeatherData
import com.example.smartdryingrack.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val sensorData: SensorData? = null,
    val rackState: RackState = RackState.UNKNOWN,
    val rackMode: RackMode = RackMode.AUTO,
    val connection: ConnectionState = ConnectionState.OFFLINE,
    val rainConfirmed: Boolean = false,
    val weather: WeatherData? = null,
    val dryThreshold: Int = 90
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    rack: RackRepository,
    weather: WeatherRepository,
    settings: SettingsRepository
) : ViewModel() {

    private val base = combine(
        rack.sensorData, rack.rackState, rack.mode, rack.connection, rack.rainConfirmed
    ) { sensor, state, mode, conn, rain ->
        HomeUiState(sensorData = sensor, rackState = state, rackMode = mode, connection = conn, rainConfirmed = rain)
    }

    val uiState: StateFlow<HomeUiState> = combine(base, weather.forecast, settings.settings) { b, w, s ->
        b.copy(weather = w, dryThreshold = s.dryThresholdPercent)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
}
