package com.example.smartdryingrack.feature.alerts

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.domain.model.EventType
import com.example.smartdryingrack.domain.model.Severity

@StringRes
fun EventType.titleRes(): Int = when (this) {
    EventType.RAIN_DETECTED -> R.string.ev_rain_detected_t
    EventType.RAIN_RETRACT -> R.string.ev_rain_retract_t
    EventType.FORECAST_SKIP -> R.string.ev_forecast_skip_t
    EventType.FORECAST_RETRACT -> R.string.ev_forecast_retract_t
    EventType.CLOTHES_DRY -> R.string.ev_clothes_dry_t
    EventType.SCHEDULE_EXTEND -> R.string.ev_schedule_extend_t
    EventType.SCHEDULE_RETRACT -> R.string.ev_schedule_retract_t
    EventType.MANUAL_EXTEND -> R.string.ev_manual_extend_t
    EventType.MANUAL_RETRACT -> R.string.ev_manual_retract_t
    EventType.EMERGENCY_STOP -> R.string.ev_emergency_stop_t
    EventType.DEVICE_CONNECTED -> R.string.ev_device_connected_t
    EventType.DEVICE_DISCONNECTED -> R.string.ev_device_disconnected_t
    EventType.DEVICE_ERROR -> R.string.ev_device_error_t
    EventType.SENSOR_INVALID -> R.string.ev_sensor_invalid_t
}

@StringRes
fun EventType.descRes(): Int = when (this) {
    EventType.RAIN_DETECTED -> R.string.ev_rain_detected_d
    EventType.RAIN_RETRACT -> R.string.ev_rain_retract_d
    EventType.FORECAST_SKIP -> R.string.ev_forecast_skip_d
    EventType.FORECAST_RETRACT -> R.string.ev_forecast_retract_d
    EventType.CLOTHES_DRY -> R.string.ev_clothes_dry_d
    EventType.SCHEDULE_EXTEND -> R.string.ev_schedule_extend_d
    EventType.SCHEDULE_RETRACT -> R.string.ev_schedule_retract_d
    EventType.MANUAL_EXTEND -> R.string.ev_manual_extend_d
    EventType.MANUAL_RETRACT -> R.string.ev_manual_retract_d
    EventType.EMERGENCY_STOP -> R.string.ev_emergency_stop_d
    EventType.DEVICE_CONNECTED -> R.string.ev_device_connected_d
    EventType.DEVICE_DISCONNECTED -> R.string.ev_device_disconnected_d
    EventType.DEVICE_ERROR -> R.string.ev_device_error_d
    EventType.SENSOR_INVALID -> R.string.ev_sensor_invalid_d
}

@Composable
fun Severity.color(): Color = when (this) {
    Severity.INFO -> AppColors.Primary
    Severity.SUCCESS -> AppColors.Success
    Severity.WARNING -> AppColors.Warning
    Severity.ERROR -> AppColors.Danger
}

fun eventTime(context: Context, timestamp: Long): String = DateUtils.formatDateTime(
    context,
    timestamp,
    DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_ALL
)

fun EventType.eventIcon(): androidx.compose.ui.graphics.vector.ImageVector = when (this) {
    EventType.RAIN_DETECTED, EventType.RAIN_RETRACT, EventType.FORECAST_RETRACT, EventType.FORECAST_SKIP -> androidx.compose.material.icons.Icons.Outlined.Umbrella
    EventType.CLOTHES_DRY -> androidx.compose.material.icons.Icons.Outlined.CheckCircle
    EventType.MANUAL_EXTEND, EventType.SCHEDULE_EXTEND -> androidx.compose.material.icons.Icons.Outlined.KeyboardDoubleArrowUp
    EventType.MANUAL_RETRACT, EventType.SCHEDULE_RETRACT -> androidx.compose.material.icons.Icons.Outlined.KeyboardDoubleArrowDown
    EventType.DEVICE_CONNECTED, EventType.DEVICE_DISCONNECTED -> androidx.compose.material.icons.Icons.Outlined.Wifi
    EventType.EMERGENCY_STOP -> androidx.compose.material.icons.Icons.Outlined.StopCircle
    EventType.DEVICE_ERROR, EventType.SENSOR_INVALID -> androidx.compose.material.icons.Icons.Outlined.Warning
}
