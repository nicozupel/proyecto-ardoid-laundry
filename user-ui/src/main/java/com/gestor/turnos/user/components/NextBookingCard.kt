package com.gestor.turnos.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun NextBookingCard(
    booking: Booking?,
    onStartBooking: () -> Unit,
    onCancelBooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Próximo Turno",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            if (booking != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Máquina: ${booking.machineId}", // TODO: Get actual machine name
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = formatBookingTime(booking),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Duración: ${booking.durationMinutes} minutos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                if (booking.status == BookingStatus.RESERVADO) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onStartBooking,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                            Text("Iniciar", modifier = Modifier.padding(start = 4.dp))
                        }
                        OutlinedButton(
                            onClick = onCancelBooking,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null
                            )
                            Text("Cancelar", modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            } else {
                Text(
                    text = "No tienes turnos programados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

private fun formatBookingTime(booking: Booking): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
    return "${booking.startDateTime.format(formatter)} - ${booking.endDateTime.format(formatter)}"
}

@Preview
@Composable
fun NextBookingCardPreview() {
    GestorTurnosLaundryTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            NextBookingCard(
                booking = Booking(
                    id = "1",
                    machineId = "Lavarropas 1",
                    userId = "user1",
                    startDateTime = LocalDateTime.now().plusHours(2),
                    endDateTime = LocalDateTime.now().plusHours(4),
                    status = BookingStatus.RESERVADO
                ),
                onStartBooking = {},
                onCancelBooking = {}
            )

            NextBookingCard(
                booking = null,
                onStartBooking = {},
                onCancelBooking = {}
            )
        }
    }
}