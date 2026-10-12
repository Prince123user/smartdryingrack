package com.example.smartdryingrack.data.repository

import android.content.Context
import com.example.smartdryingrack.data.hardware.rest.normalizeRackUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevicePreferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("rack_device", Context.MODE_PRIVATE)
    private val _address = MutableStateFlow(prefs.getString("address", "") ?: "")
    val address = _address.asStateFlow()
    fun saveAddress(value: String) {
        val normalized = normalizeRackUrl(value)
        prefs.edit().putString("address", normalized).apply()
        _address.value = normalized
    }
}
