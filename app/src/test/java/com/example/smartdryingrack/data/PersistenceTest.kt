package com.example.smartdryingrack.data

import com.example.smartdryingrack.data.repository.*
import com.example.smartdryingrack.domain.model.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalTime

class PersistenceTest {
    @Test fun scheduleRoundTripPreservesAllChoices() {
        val schedule = RackSchedule(enabled = false, extendTime = LocalTime.of(6, 15), retractTime = LocalTime.of(16, 45),
            monday = false, tuesday = true, wednesday = false, thursday = true, friday = false, saturday = true, sunday = true)
        assertEquals(schedule, decodeSchedule(encodeSchedule(schedule)))
    }
    @Test fun eventRoundTripPreservesOrderIdsDatesAndReadState() {
        val events = listOf(RackEvent(42, EventType.RAIN_DETECTED, 1000, true), RackEvent(40, EventType.DEVICE_DISCONNECTED, 900, false))
        assertEquals(events, decodeEvents(encodeEvents(events)))
        assertEquals(emptyList<RackEvent>(), decodeEvents(encodeEvents(emptyList())))
    }
}
