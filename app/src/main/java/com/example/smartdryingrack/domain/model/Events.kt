package com.example.smartdryingrack.domain.model

enum class AlertCategory { RAIN, DRY, SYSTEM, SCHEDULE }

enum class Severity { INFO, SUCCESS, WARNING, ERROR }

enum class EventType(val category: AlertCategory, val severity: Severity, val isAlert: Boolean) {
    RAIN_DETECTED(AlertCategory.RAIN, Severity.WARNING, true),
    RAIN_RETRACT(AlertCategory.RAIN, Severity.WARNING, true),
    FORECAST_SKIP(AlertCategory.RAIN, Severity.INFO, true),
    FORECAST_RETRACT(AlertCategory.RAIN, Severity.INFO, true),
    CLOTHES_DRY(AlertCategory.DRY, Severity.SUCCESS, true),
    SCHEDULE_EXTEND(AlertCategory.SCHEDULE, Severity.INFO, true),
    SCHEDULE_RETRACT(AlertCategory.SCHEDULE, Severity.INFO, true),
    MANUAL_EXTEND(AlertCategory.SYSTEM, Severity.INFO, false),
    MANUAL_RETRACT(AlertCategory.SYSTEM, Severity.INFO, false),
    EMERGENCY_STOP(AlertCategory.SYSTEM, Severity.WARNING, true),
    DEVICE_CONNECTED(AlertCategory.SYSTEM, Severity.INFO, false),
    DEVICE_DISCONNECTED(AlertCategory.SYSTEM, Severity.ERROR, true),
    DEVICE_ERROR(AlertCategory.SYSTEM, Severity.ERROR, true),
    SENSOR_INVALID(AlertCategory.SYSTEM, Severity.ERROR, true)
}

data class RackEvent(
    val id: Long,
    val type: EventType,
    val timestamp: Long,
    val read: Boolean = false
)
