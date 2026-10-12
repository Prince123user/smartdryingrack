package com.example.smartdryingrack.feature.settings

import androidx.lifecycle.ViewModel
import com.example.smartdryingrack.data.repository.SettingsRepository
import com.example.smartdryingrack.domain.model.AutomationSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val settings: StateFlow<AutomationSettings> = settingsRepository.settings

    fun update(transform: (AutomationSettings) -> AutomationSettings) {
        settingsRepository.update(transform)
    }
}
