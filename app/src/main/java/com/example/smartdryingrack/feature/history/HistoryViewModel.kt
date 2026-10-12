package com.example.smartdryingrack.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdryingrack.data.repository.EventLog
import com.example.smartdryingrack.domain.model.EventType
import com.example.smartdryingrack.domain.model.RackEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HistoryUiState(
    val rainSaves: Int = 0,
    val manualOps: Int = 0,
    val autoOps: Int = 0,
    val dryAlerts: Int = 0,
    val recent: List<RackEvent> = emptyList()
)

private val MANUAL = setOf(EventType.MANUAL_EXTEND, EventType.MANUAL_RETRACT)
private val AUTOMATIC = setOf(
    EventType.RAIN_RETRACT, EventType.FORECAST_RETRACT, EventType.SCHEDULE_EXTEND, EventType.SCHEDULE_RETRACT
)

@HiltViewModel
class HistoryViewModel @Inject constructor(log: EventLog) : ViewModel() {
    val uiState: StateFlow<HistoryUiState> = log.events.map { events ->
        HistoryUiState(
            rainSaves = events.count { it.type == EventType.RAIN_RETRACT },
            manualOps = events.count { it.type in MANUAL },
            autoOps = events.count { it.type in AUTOMATIC },
            dryAlerts = events.count { it.type == EventType.CLOTHES_DRY },
            recent = events
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())
}
