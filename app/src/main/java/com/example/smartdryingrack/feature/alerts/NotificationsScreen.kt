package com.example.smartdryingrack.feature.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.*
import com.example.smartdryingrack.data.repository.EventLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val log: EventLog) : ViewModel() {
    val events = log.events
    fun markRead(id: Long) = log.markRead(id)
    fun markAllRead() = log.markAllRead()
}

@Composable
fun NotificationsScreen(onBack: () -> Unit, viewModel: NotificationsViewModel = hiltViewModel()) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    ScreenColumn {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                }
                Text("Notifications", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = viewModel::markAllRead, enabled = events.any { !it.read }) { Text("Mark all read") }
        }
        if (events.isEmpty()) EmptyStateCard("No notifications yet", "Connection changes and accepted commands will appear in your event inbox.", Icons.Outlined.NotificationsNone)
        events.forEach { event -> key(event.id) { EventCard(event, viewModel::markRead, showBadge = false) } }
    }
}
