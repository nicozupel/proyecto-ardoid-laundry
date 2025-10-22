package com.gestor.turnos.user.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.laundry.ui.theme.MachineAvailable
import com.gestor.turnos.laundry.ui.theme.MachineGrace
import com.gestor.turnos.laundry.ui.theme.MachineInUse
import com.gestor.turnos.laundry.ui.theme.MachineMaintenance
import com.gestor.turnos.laundry.ui.theme.MachineReserved
import com.gestor.turnos.user.calendar.TimeSlot
import java.time.LocalDateTime

@Composable
fun TimeSlotCell(
    slot: TimeSlot,
    isSelectable: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        slot.isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        slot.isOwnBooking -> MachineReserved.copy(alpha = 0.7f)
        !slot.isAvailable && slot.bookingId != null -> MachineInUse.copy(alpha = 0.5f)
        !slot.isAvailable -> MachineMaintenance.copy(alpha = 0.5f)
        else -> MachineAvailable.copy(alpha = 0.1f)
    }

    val borderColor = when {
        slot.isSelected -> MaterialTheme.colorScheme.primary
        slot.isOwnBooking -> MachineReserved
        !slot.isAvailable -> Color.Gray
        else -> Color.Transparent
    }

    val textColor = when {
        slot.isSelected -> MaterialTheme.colorScheme.primary
        slot.isOwnBooking -> Color.White
        !slot.isAvailable -> Color.Gray
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = if (slot.isSelected || slot.isOwnBooking) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(enabled = isSelectable && slot.isAvailable) { onClick() }
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        if (slot.isOwnBooking) {
            Text(
                text = "Mío",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                textAlign = TextAlign.Center
            )
        } else if (!slot.isAvailable && slot.bookingId != null) {
            Text(
                text = "●",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = textColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun TimeSlotCellPreview() {
    GestorTurnosLaundryTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Available slot
            TimeSlotCell(
                slot = TimeSlot(
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusMinutes(30),
                    isAvailable = true
                )
            )

            // Selected slot
            TimeSlotCell(
                slot = TimeSlot(
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusMinutes(30),
                    isAvailable = true,
                    isSelected = true
                )
            )

            // Own booking
            TimeSlotCell(
                slot = TimeSlot(
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusMinutes(30),
                    isAvailable = false,
                    isOwnBooking = true,
                    bookingId = "booking1"
                )
            )

            // Other's booking
            TimeSlotCell(
                slot = TimeSlot(
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusMinutes(30),
                    isAvailable = false,
                    bookingId = "booking2"
                )
            )

            // Unavailable
            TimeSlotCell(
                slot = TimeSlot(
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusMinutes(30),
                    isAvailable = false
                )
            )
        }
    }
}