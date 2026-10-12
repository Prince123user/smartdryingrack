package com.example.smartdryingrack.data.repository

import com.example.smartdryingrack.domain.model.AutomationSettings
import com.example.smartdryingrack.domain.model.RackSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/** In-memory for now; DataStore-backed in the settings phase. */
@Singleton
class SettingsRepository @Inject constructor() {
    private val _settings = MutableStateFlow(AutomationSettings())
    val settings: StateFlow<AutomationSettings> = _settings.asStateFlow()
    fun update(transform: (AutomationSettings) -> AutomationSettings) = _settings.update(transform)
}

/** In-memory for now; Room-backed in the persistence phase. */
@Singleton
class ScheduleRepository @Inject constructor() {
    private val _schedule = MutableStateFlow(RackSchedule())
    val schedule: StateFlow<RackSchedule> = _schedule.asStateFlow()
    fun update(transform: (RackSchedule) -> RackSchedule) = _schedule.update(transform)
}
