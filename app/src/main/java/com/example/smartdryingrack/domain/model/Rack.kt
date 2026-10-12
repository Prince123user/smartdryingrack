package com.example.smartdryingrack.domain.model

enum class RackState { EXTENDED, RETRACTED, EXTENDING, RETRACTING, STOPPED, ERROR, UNKNOWN }

enum class RackMode { AUTO, MANUAL }

enum class RackAction { EXTEND, RETRACT, STOP, WAIT, NONE }

enum class ConnectionState { ONLINE, OFFLINE, CONNECTING, ERROR }

/** Declaration order is priority order: lower ordinal wins. */
enum class CommandSource { EMERGENCY, SAFETY, RAIN, MANUAL, WEATHER, SCHEDULE, AUTO }

data class LastCommand(val action: RackAction, val source: CommandSource, val timestamp: Long)

data class SensorData(
    val temperature: Float,
    val humidity: Float,
    val rainDetected: Boolean,
    val clothesDryPercentage: Int,
    val timestamp: Long
) {
    val isValid: Boolean
        get() = temperature.isFinite() && temperature in -50f..80f &&
            humidity.isFinite() && humidity in 0f..100f &&
            clothesDryPercentage in 0..100
}

data class WeatherData(
    val temperature: Double,
    val humidity: Int,
    val rainProbability: Int,
    val condition: String,
    val windSpeed: Double,
    val forecastTime: Long,
    val isSimulated: Boolean = false
)

enum class Unavailable { OFFLINE, AUTO_MODE, ALREADY_EXTENDED, ALREADY_RETRACTED, MOVING }

data class ControlAvailability(val extend: Unavailable?, val retract: Unavailable?, val stop: Unavailable?)

fun computeAvailability(state: RackState, mode: RackMode, connection: ConnectionState): ControlAvailability {
    if (connection != ConnectionState.ONLINE) {
        return ControlAvailability(Unavailable.OFFLINE, Unavailable.OFFLINE, Unavailable.OFFLINE)
    }
    val moving = state == RackState.EXTENDING || state == RackState.RETRACTING
    val extend = when {
        mode == RackMode.AUTO -> Unavailable.AUTO_MODE
        state == RackState.EXTENDED -> Unavailable.ALREADY_EXTENDED
        moving -> Unavailable.MOVING
        else -> null
    }
    val retract = when {
        mode == RackMode.AUTO -> Unavailable.AUTO_MODE
        state == RackState.RETRACTED -> Unavailable.ALREADY_RETRACTED
        moving -> Unavailable.MOVING
        else -> null
    }
    return ControlAvailability(extend, retract, null)
}

sealed interface CommandResult {
    data object Accepted : CommandResult
    data class Rejected(val reason: RejectReason) : CommandResult
}

enum class RejectReason { OFFLINE, ALREADY_THERE, BUSY, ERROR, EMERGENCY_HOLD, NOT_A_COMMAND }
