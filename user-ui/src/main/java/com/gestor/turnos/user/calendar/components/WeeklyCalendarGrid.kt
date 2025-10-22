package com.gestor.turnos.user.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.user.calendar.CalendarUtils
import com.gestor.turnos.user.calendar.DayColumn
import com.gestor.turnos.user.calendar.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun WeeklyCalendarGrid(
    dayColumns: List<DayColumn>,
    selectedSlots: Set<TimeSlot>,
    onSlotClick: (TimeSlot, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeLabels = generateTimeLabels()

    Column(modifier = modifier) {
        // Week header
        WeekHeader(days = dayColumns.map { it.date })

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        LazyColumn {
            itemsIndexed(timeLabels) { index, timeLabel ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time label
                    Box(
                        modifier = Modifier.width(60.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = timeLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    // Time slots for each day
                    dayColumns.forEach { dayColumn ->
                        Box(modifier = Modifier.weight(1f)) {
                            if (index < dayColumn.slots.size) {
                                val slot = dayColumn.slots[index]
                                TimeSlotCell(
                                    slot = slot.copy(isSelected = selectedSlots.contains(slot)),
                                    onClick = { onSlotClick(slot, index) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun generateTimeLabels(): List<String> {
    val labels = mutableListOf<String>()
    var currentTime = LocalTime.of(7, 0) // Start at 7:00 AM

    while (currentTime.isBefore(LocalTime.of(23, 0))) { // End at 11:00 PM
        labels.add(CalendarUtils.formatTimeLabel(currentTime))
        currentTime = currentTime.plusMinutes(30)
    }

    return labels
}

@Preview
@Composable
fun WeeklyCalendarGridPreview() {
    GestorTurnosLaundryTheme {
        val today = LocalDate.now()
        val weekDays = CalendarUtils.generateWeekDays(today)

        val dayColumns = weekDays.map { date ->
            DayColumn(
                date = date,
                slots = CalendarUtils.generateTimeSlots(date).mapIndexed { index, slot ->
                    when {
                        index == 5 -> slot.copy(isOwnBooking = true, isAvailable = false, bookingId = "my-booking")
                        index == 6 -> slot.copy(isAvailable = false, bookingId = "other-booking")
                        index in 10..12 -> slot.copy(isAvailable = false)
                        else -> slot
                    }
                }
            )
        }

        WeeklyCalendarGrid(
            dayColumns = dayColumns,
            selectedSlots = emptySet(),
            onSlotClick = { _, _ -> },
            modifier = Modifier.padding(16.dp)
        )
    }
}