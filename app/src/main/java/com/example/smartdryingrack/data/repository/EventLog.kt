package com.example.smartdryingrack.data.repository

import com.example.smartdryingrack.domain.model.EventType
import com.example.smartdryingrack.domain.model.RackEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/** In-memory event log. Becomes a Room-backed repository in the history phase. */
@Singleton
class EventLog @Inject constructor() {
    private val nextId = AtomicLong(0)
    private val _events = MutableStateFlow<List<RackEvent>>(emptyList())
    val events: StateFlow<List<RackEvent>> = _events.asStateFlow()

    fun record(type: EventType) {
        val event = RackEvent(nextId.incrementAndGet(), type, System.currentTimeMillis())
        _events.update { (listOf(event) + it).take(MAX_EVENTS) }
    }

    fun markRead(id: Long) = _events.update { list -> list.map { if (it.id == id) it.copy(read = true) else it } }

    fun markAllRead() = _events.update { list -> list.map { it.copy(read = true) } }

    fun clear() { _events.value = emptyList() }

    private companion object {
        const val MAX_EVENTS = 100
    }
}
