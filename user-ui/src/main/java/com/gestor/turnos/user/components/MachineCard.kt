package com.gestor.turnos.user.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme

@Composable
fun MachineCard(
    machine: Machine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = machine.type.getIcon(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = machine.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                MachineStatusChip(status = machine.status)
            }

            Text(
                text = machine.type.getDisplayName(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun MachineType.getIcon(): ImageVector {
    return when (this) {
        MachineType.LAVARROPAS -> Icons.Default.LocalLaundryService
        MachineType.SECARROPAS -> Icons.Default.Whatshot
    }
}

private fun MachineType.getDisplayName(): String {
    return when (this) {
        MachineType.LAVARROPAS -> "Lavarropas"
        MachineType.SECARROPAS -> "Secarropas"
    }
}

@Preview
@Composable
fun MachineCardPreview() {
    GestorTurnosLaundryTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MachineCard(
                machine = Machine(
                    id = "1",
                    roomId = "room1",
                    name = "Lavarropas 1",
                    type = MachineType.LAVARROPAS,
                    status = MachineState.LIBRE
                ),
                onClick = {}
            )
            MachineCard(
                machine = Machine(
                    id = "2",
                    roomId = "room1",
                    name = "Secarropas 1",
                    type = MachineType.SECARROPAS,
                    status = MachineState.EN_USO
                ),
                onClick = {}
            )
        }
    }
}