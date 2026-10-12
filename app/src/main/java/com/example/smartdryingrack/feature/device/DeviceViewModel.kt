package com.example.smartdryingrack.feature.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdryingrack.data.repository.RackRepository
import com.example.smartdryingrack.domain.model.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val rackRepository: RackRepository
) : ViewModel() {
    val connection: StateFlow<ConnectionState> = rackRepository.connection

    fun reconnect() {
        viewModelScope.launch {
            rackRepository.connect()
        }
    }
}
