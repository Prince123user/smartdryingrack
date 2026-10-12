package com.example.smartdryingrack.feature.control

import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.EmergencyStopButton
import com.example.smartdryingrack.core.designsystem.FuturisticToggle
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ModeSelector
import com.example.smartdryingrack.core.designsystem.RackControlButton
import com.example.smartdryingrack.core.designsystem.RackStatusCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.labelRes
import com.example.smartdryingrack.domain.model.CommandSource
import com.example.smartdryingrack.domain.model.RackAction
import com.example.smartdryingrack.domain.model.RejectReason
import com.example.smartdryingrack.domain.model.Unavailable
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ControlRoute(viewModel: ControlViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ControlScreen(state, viewModel)
}

@StringRes
private fun Unavailable.res(): Int = when (this) {
    Unavailable.OFFLINE -> R.string.unavailable_offline
    Unavailable.AUTO_MODE -> R.string.unavailable_auto
    Unavailable.ALREADY_EXTENDED -> R.string.unavailable_extended
    Unavailable.ALREADY_RETRACTED -> R.string.unavailable_retracted
    Unavailable.MOVING -> R.string.unavailable_moving
}

@StringRes
private fun RejectReason.res(): Int = when (this) {
    RejectReason.OFFLINE -> R.string.reject_offline
    RejectReason.ALREADY_THERE -> R.string.reject_already
    RejectReason.BUSY -> R.string.reject_busy
    RejectReason.ERROR -> R.string.reject_error
    RejectReason.EMERGENCY_HOLD -> R.string.reject_hold
    RejectReason.NOT_A_COMMAND -> R.string.reject_none
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(state: ControlUiState, actions: ControlActions) {
    ScreenColumn {
        Text("Control & Schedule", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)

        ModeSelector(state.mode, actions::setMode, Modifier.fillMaxWidth())

        RackStatusCard(state.rackState, false, "Clear skies")

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RackControlButton(
                label = "▲ Extend",
                onClick = { actions.send(RackAction.EXTEND) },
                enabled = state.availability.extend == null,
                modifier = Modifier.weight(1f)
            )
            RackControlButton(
                label = "■ Stop",
                onClick = { actions.send(RackAction.STOP) },
                enabled = state.availability.stop == null,
                modifier = Modifier.weight(1f),
                accent = AppColors.Warning
            )
            RackControlButton(
                label = "▼ Retract",
                onClick = { actions.send(RackAction.RETRACT) },
                enabled = state.availability.retract == null,
                modifier = Modifier.weight(1f)
            )
        }

        EmergencyStopButton(onClick = actions::emergencyStop, modifier = Modifier.fillMaxWidth())

        state.rejection?.let {
            Text(stringResource(it.res()), color = AppColors.Warning, style = MaterialTheme.typography.bodyMedium)
        }

        ScheduleCard(state, actions)
        DemoCard(state, actions)
    }
}

@Composable
private fun DemoCard(state: ControlUiState, actions: ControlActions) {
    GlassCard(Modifier.fillMaxWidth(), accent = AppColors.Warning) {
        Text("Hardware Simulation & Demo", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        FuturisticToggle("Simulate Rain Sensor", state.demo.rain) { actions.setDemoRain(it) }
        FuturisticToggle("Simulate Sensor Fault", state.demo.sensorError) { actions.setDemoSensorError(it) }
        FuturisticToggle("Simulate Disconnection", state.demo.offline) { actions.setDemoOffline(it) }
        Spacer(Modifier.height(8.dp))
        RackControlButton(
            "Trigger Clothes Dry Event",
            actions::demoClothesDry,
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            accent = AppColors.Success
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleCard(state: ControlUiState, actions: ControlActions) {
    var editing by remember { mutableStateOf<Boolean?>(null) }
    val schedule = state.schedule
    val formatter = remember { DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT) }

    GlassCard(Modifier.fillMaxWidth()) {
        Text("Schedule", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        FuturisticToggle("Schedule Enabled", schedule.enabled) { actions.setScheduleEnabled(it) }
        TimeRow("Rack Out", formatter.format(schedule.extendTime)) { editing = true }
        TimeRow("Rack In", formatter.format(schedule.retractTime)) { editing = false }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DayOfWeek.entries.forEach { day ->
                val full = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
                FilterChip(
                    selected = schedule.isDayEnabled(day),
                    onClick = { actions.setDay(day, !schedule.isDayEnabled(day)) },
                    label = { Text(day.getDisplayName(TextStyle.NARROW, Locale.getDefault())) },
                    modifier = Modifier.semantics { contentDescription = full }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        FuturisticToggle("Weather Protection", state.settings.useWeather) { actions.setUseWeather(it) }
        FuturisticToggle("Buzzer Sound", state.settings.buzzer) { actions.setBuzzer(it) }
    }

    editing?.let { isExtend ->
        TimeDialog(
            initial = if (isExtend) schedule.extendTime else schedule.retractTime,
            onDismiss = { editing = null },
            onConfirm = {
                if (isExtend) actions.setExtendTime(it) else actions.setRetractTime(it)
                editing = null
            }
        )
    }
}

@Composable
private fun TimeRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = Dimens.MinTouch).clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColors.TextPrimary)
        Text(value, color = AppColors.Primary, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeDialog(initial: LocalTime, onDismiss: () -> Unit, onConfirm: (LocalTime) -> Unit) {
    val picker = rememberTimePickerState(
        initialHour = initial.hour,
        initialMinute = initial.minute,
        is24Hour = DateFormat.is24HourFormat(LocalContext.current)
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(LocalTime.of(picker.hour, picker.minute)) }) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        text = { TimePicker(state = picker) }
    )
}
