package com.example.smartdryingrack.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdryingrack.data.repository.EventLog
import com.example.smartdryingrack.domain.model.AlertCategory
import com.example.smartdryingrack.domain.model.RackEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AlertsUiState(val filter: AlertCategory? = null, val alerts: List<RackEvent> = emptyList())

@HiltViewModel
class AlertsViewModel @Inject constructor(private val log: EventLog) : ViewModel() {
    private val filter = MutableStateFlow<AlertCategory?>(null)

    val uiState: StateFlow<AlertsUiState> = combine(log.events, filter) { events, f ->
        AlertsUiState(
            filter = f,
            alerts = events.filter { it.type.isAlert && (f == null || it.type.category == f) }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AlertsUiState())

    fun setFilter(category: AlertCategory?) { filter.value = category }
    fun markRead(id: Long) = log.markRead(id)
    fun markAllRead() = log.markAllRead()
    fun clearAll() = log.clear()
}
