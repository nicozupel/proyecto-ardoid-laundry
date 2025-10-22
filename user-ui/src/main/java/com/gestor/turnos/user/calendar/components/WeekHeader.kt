package com.gestor.turnos.user.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekHeader(
    days: List<LocalDate>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // Empty space for time labels column
        Spacer(modifier = Modifier.width(60.dp))

        days.forEach { date ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("dd")),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (date == LocalDate.now()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun WeekHeaderPreview() {
    GestorTurnosLaundryTheme {
        val today = LocalDate.now()
        val weekDays = (0..6).map { today.plusDays(it.toLong()) }

        WeekHeader(
            days = weekDays,
            modifier = Modifier.padding(16.dp)
        )
    }
}