package com.example.smartdryingrack.data.repository

import android.content.Context
import com.example.smartdryingrack.domain.model.EventType
import com.example.smartdryingrack.domain.model.RackEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

fun encodeEvents(events: List<RackEvent>): String = JSONArray().apply {
    events.forEach { event -> put(JSONObject().put("id", event.id).put("type", event.type.name)
        .put("timestamp", event.timestamp).put("read", event.read)) }
}.toString()

fun decodeEvents(value: String): List<RackEvent> {
    val array = JSONArray(value)
    return (0 until array.length()).map { index ->
        val item = array.getJSONObject(index)
        RackEvent(item.getLong("id"), EventType.valueOf(item.getString("type")), item.getLong("timestamp"), item.getBoolean("read"))
    }
}

@Singleton
class EventLog @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("rack_events", Context.MODE_PRIVATE)
    private val _events = MutableStateFlow(runCatching { decodeEvents(prefs.getString("events", "[]") ?: "[]") }
        .getOrDefault(emptyList()).take(100))
    val events = _events.asStateFlow()
    private var nextId = _events.value.maxOfOrNull { it.id } ?: 0L

    @Synchronized fun record(type: EventType) = save((listOf(RackEvent(++nextId, type, System.currentTimeMillis())) + _events.value).take(100))
    @Synchronized fun markRead(id: Long) = save(_events.value.map { if (it.id == id) it.copy(read = true) else it })
    @Synchronized fun markAllRead() = save(_events.value.map { it.copy(read = true) })
    @Synchronized fun clear() = save(emptyList())
    private fun save(events: List<RackEvent>) {
        prefs.edit().putString("events", encodeEvents(events)).apply()
        _events.value = events
    }
}
