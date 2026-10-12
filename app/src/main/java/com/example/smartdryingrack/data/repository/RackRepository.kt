package com.example.smartdryingrack.data.repository

import com.example.smartdryingrack.data.hardware.RackController
import com.example.smartdryingrack.domain.model.CommandResult
import com.example.smartdryingrack.domain.model.CommandSource
import com.example.smartdryingrack.domain.model.ConnectionState
import com.example.smartdryingrack.domain.model.EventType
import com.example.smartdryingrack.domain.model.LastCommand
import com.example.smartdryingrack.domain.model.RackAction
import com.example.smartdryingrack.domain.model.RackMode
import com.example.smartdryingrack.domain.model.RackState
import com.example.smartdryingrack.domain.model.RejectReason
import com.example.smartdryingrack.domain.model.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single gateway for rack commands. Enforces: one command at a time, no repeated or
 * conflicting commands, priority between sources, movement timeout, and emergency hold.
 */
@Singleton
class RackRepository @Inject constructor(
    private val controller: RackController,
    private val events: EventLog
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutex = Mutex()
    private val fault = MutableStateFlow(false)
    private var emergencyHold = false

    private val _mode = MutableStateFlow(RackMode.AUTO)
    val mode: StateFlow<RackMode> = _mode.asStateFlow()

    private val _lastCommand = MutableStateFlow<LastCommand?>(null)
    val lastCommand: StateFlow<LastCommand?> = _lastCommand.asStateFlow()

    private val _rainConfirmed = MutableStateFlow(false)
    val rainConfirmed: StateFlow<Boolean> = _rainConfirmed.asStateFlow()

    val rackState: StateFlow<RackState> =
        combine(controller.observeRackState(), fault) { state, faulted ->
            if (faulted) RackState.ERROR else state
        }.stateIn(scope, SharingStarted.Eagerly, RackState.UNKNOWN)

    val sensorData: StateFlow<SensorData?> =
        controller.observeSensorData().stateIn(scope, SharingStarted.Eagerly, null)

    val connection: StateFlow<ConnectionState> =
        controller.observeConnection().stateIn(scope, SharingStarted.Eagerly, ConnectionState.OFFLINE)

    suspend fun connect() = controller.connect()

    fun setMode(mode: RackMode) {
        _mode.value = mode
        if (mode == RackMode.AUTO) emergencyHold = false
    }

    fun setRainConfirmed(value: Boolean) { _rainConfirmed.value = value }

    suspend fun send(action: RackAction, source: CommandSource): CommandResult =
        mutex.withLock { sendLocked(action, source) }

    private suspend fun sendLocked(action: RackAction, source: CommandSource): CommandResult {
        if (action != RackAction.EXTEND && action != RackAction.RETRACT && action != RackAction.STOP) {
            return CommandResult.Rejected(RejectReason.NOT_A_COMMAND)
        }
        if (connection.value != ConnectionState.ONLINE) return CommandResult.Rejected(RejectReason.OFFLINE)

        if (action == RackAction.STOP) {
            try {
                controller.stop()
            } catch (e: Exception) {
                return CommandResult.Rejected(RejectReason.ERROR)
            }
            if (source == CommandSource.EMERGENCY) {
                emergencyHold = true
                events.record(EventType.EMERGENCY_STOP)
            }
            _lastCommand.value = LastCommand(action, source, System.currentTimeMillis())
            return CommandResult.Accepted
        }

        if (emergencyHold) {
            if (source == CommandSource.MANUAL) emergencyHold = false
            else return CommandResult.Rejected(RejectReason.EMERGENCY_HOLD)
        }

        val state = rackState.value
        val target = if (action == RackAction.EXTEND) RackState.EXTENDED else RackState.RETRACTED
        val sameMotion = (action == RackAction.EXTEND && state == RackState.EXTENDING) ||
            (action == RackAction.RETRACT && state == RackState.RETRACTING)
        if (state == target || sameMotion) return CommandResult.Rejected(RejectReason.ALREADY_THERE)

        val moving = state == RackState.EXTENDING || state == RackState.RETRACTING
        try {
            if (moving) {
                if (source.ordinal <= CommandSource.RAIN.ordinal) controller.stop()
                else return CommandResult.Rejected(RejectReason.BUSY)
            }
            fault.value = false
            if (action == RackAction.EXTEND) controller.extend() else controller.retract()
        } catch (e: Exception) {
            return CommandResult.Rejected(RejectReason.ERROR)
        }

        _lastCommand.value = LastCommand(action, source, System.currentTimeMillis())
        eventFor(action, source)?.let { events.record(it) }
        watchMovement(target)
        return CommandResult.Accepted
    }

    private fun watchMovement(target: RackState) {
        scope.launch {
            val reached = withTimeoutOrNull(MOVE_TIMEOUT_MS) {
                controller.observeRackState().first { it == target }
            }
            val s = rackState.value
            if (reached == null && (s == RackState.EXTENDING || s == RackState.RETRACTING)) {
                runCatching { controller.stop() }
                fault.value = true
                events.record(EventType.DEVICE_ERROR)
            }
        }
    }

    private fun eventFor(action: RackAction, source: CommandSource): EventType? = when (source) {
        CommandSource.MANUAL ->
            if (action == RackAction.EXTEND) EventType.MANUAL_EXTEND else EventType.MANUAL_RETRACT
        CommandSource.RAIN -> EventType.RAIN_RETRACT
        CommandSource.WEATHER -> if (action == RackAction.RETRACT) EventType.FORECAST_RETRACT else null
        CommandSource.SCHEDULE ->
            if (action == RackAction.EXTEND) EventType.SCHEDULE_EXTEND else EventType.SCHEDULE_RETRACT
        else -> null
    }

    private companion object {
        const val MOVE_TIMEOUT_MS = 10_000L
    }
}
