package com.gestor.turnos.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookingListItem(
    booking: Booking,
    onStart: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (booking.status) {
                BookingStatus.EN_USO -> MaterialTheme.colorScheme.primaryContainer
                BookingStatus.COMPLETADO -> MaterialTheme.colorScheme.surfaceVariant
                BookingStatus.CANCELADO -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Máquina: ${booking.machineId}", // TODO: Get actual machine name
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                BookingStatusChip(status = booking.status)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatBookingDateTime(booking),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Duración: ${booking.durationMinutes} minutos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Action buttons
            if (booking.status == BookingStatus.RESERVADO) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (booking.canBeModified()) {
                        IconButton(onClick = onEdit) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = onCancel) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancelar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    if (booking.startDateTime.minusMinutes(30).isBefore(LocalDateTime.now())) {
                        IconButton(onClick = onStart) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Iniciar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            booking.notes?.let { notes ->
                Text(
                    text = "Notas: $notes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BookingStatusChip(status: BookingStatus) {
    val (text, color) = when (status) {
        BookingStatus.RESERVADO -> "Reservado" to MaterialTheme.colorScheme.primary
        BookingStatus.EN_USO -> "En Uso" to MaterialTheme.colorScheme.tertiary
        BookingStatus.COMPLETADO -> "Completado" to MaterialTheme.colorScheme.outline
        BookingStatus.CANCELADO -> "Cancelado" to MaterialTheme.colorScheme.error
        BookingStatus.NO_SHOW -> "No presentado" to MaterialTheme.colorScheme.error
    }

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

private fun formatBookingDateTime(booking: Booking): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    return "${booking.startDateTime.format(dateFormatter)} de ${booking.startDateTime.format(timeFormatter)} a ${booking.endDateTime.format(timeFormatter)}"
}

@Preview
@Composable
fun BookingListItemPreview() {
    GestorTurnosLaundryTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            BookingListItem(
                booking = Booking(
                    id = "1",
                    machineId = "Lavarropas 1",
                    userId = "user1",
                    startDateTime = LocalDateTime.now().plusHours(2),
                    endDateTime = LocalDateTime.now().plusHours(4),
                    status = BookingStatus.RESERVADO
                )
            )

            BookingListItem(
                booking = Booking(
                    id = "2",
                    machineId = "Secarropas 1",
                    userId = "user1",
                    startDateTime = LocalDateTime.now().minusHours(1),
                    endDateTime = LocalDateTime.now().plusHours(1),
                    status = BookingStatus.EN_USO
                )
            )

            BookingListItem(
                booking = Booking(
                    id = "3",
                    machineId = "Lavarropas 2",
                    userId = "user1",
                    startDateTime = LocalDateTime.now().minusDays(1),
                    endDateTime = LocalDateTime.now().minusDays(1).plusHours(2),
                    status = BookingStatus.COMPLETADO,
                    notes = "Ciclo completado sin problemas"
                )
            )
        }
    }
}