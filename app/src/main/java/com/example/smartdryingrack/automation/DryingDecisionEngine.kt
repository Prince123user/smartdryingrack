package com.example.smartdryingrack.automation

import com.example.smartdryingrack.domain.model.CommandSource
import com.example.smartdryingrack.domain.model.RackAction
import com.example.smartdryingrack.domain.model.RackMode
import com.example.smartdryingrack.domain.model.RackState
import kotlin.math.roundToInt

data class DecisionInput(
    val rackState: RackState,
    val mode: RackMode,
    val rainConfirmed: Boolean,
    val forecastRainProbability: Int?,
    val rainThresholdPercent: Int,
    val scheduleWantsRackOut: Boolean?
)

data class DryingDecision(
    val action: RackAction,
    val reason: String,
    val source: CommandSource = CommandSource.AUTO
)

/** Pure decision logic: no Android, no I/O, fully unit-testable. */
class DryingDecisionEngine {

    fun decide(i: DecisionInput): DryingDecision {
        val isOut = i.rackState in OUT_STATES

        if (i.rainConfirmed) {
            return if (isOut) DryingDecision(RackAction.RETRACT, "Rain detected", CommandSource.RAIN)
            else DryingDecision(RackAction.NONE, "Rain detected; rack already in", CommandSource.RAIN)
        }
        if (i.rackState == RackState.ERROR) {
            return DryingDecision(RackAction.NONE, "Rack in error state", CommandSource.SAFETY)
        }
        if (i.mode == RackMode.MANUAL) {
            return DryingDecision(RackAction.NONE, "Manual mode", CommandSource.MANUAL)
        }

        val rainLikely = i.forecastRainProbability != null &&
            i.forecastRainProbability >= i.rainThresholdPercent
        if (rainLikely) {
            return when {
                isOut -> DryingDecision(RackAction.RETRACT, "Rain likely soon", CommandSource.WEATHER)
                i.scheduleWantsRackOut == true ->
                    DryingDecision(RackAction.WAIT, "Rain probability too high to extend", CommandSource.WEATHER)
                else -> DryingDecision(RackAction.NONE, "Rain likely; rack stays in", CommandSource.WEATHER)
            }
        }

        return when (i.scheduleWantsRackOut) {
            true ->
                if (i.rackState == RackState.RETRACTED)
                    DryingDecision(RackAction.EXTEND, "Scheduled extension", CommandSource.SCHEDULE)
                else DryingDecision(RackAction.NONE, "Rack already out", CommandSource.SCHEDULE)
            false ->
                if (i.rackState == RackState.EXTENDED)
                    DryingDecision(RackAction.RETRACT, "Scheduled retraction", CommandSource.SCHEDULE)
                else DryingDecision(RackAction.NONE, "Rack already in", CommandSource.SCHEDULE)
            null -> DryingDecision(RackAction.NONE, "No schedule active")
        }
    }

    fun isDry(percent: Int, thresholdPercent: Int): Boolean = percent >= thresholdPercent

    companion object {
        private val OUT_STATES =
            setOf(RackState.EXTENDED, RackState.EXTENDING, RackState.STOPPED, RackState.UNKNOWN)

        /** Slider 0..1 (Low..High) to forecast-probability threshold: 90 / 60 / 30 at 0 / 0.5 / 1. */
        fun sensitivityToThreshold(sensitivity: Float): Int =
            (90f - 60f * sensitivity.coerceIn(0f, 1f)).roundToInt()
    }
}
