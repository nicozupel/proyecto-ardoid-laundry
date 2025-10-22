package com.gestor.turnos.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.laundry.ui.theme.MachineAvailable
import com.gestor.turnos.laundry.ui.theme.MachineGrace
import com.gestor.turnos.laundry.ui.theme.MachineInUse
import com.gestor.turnos.laundry.ui.theme.MachineMaintenance
import com.gestor.turnos.laundry.ui.theme.MachineOutOfService
import com.gestor.turnos.laundry.ui.theme.MachineReserved

@Composable
fun MachineStatusChip(
    status: MachineState,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (status) {
        MachineState.LIBRE -> MachineAvailable to "Disponible"
        MachineState.RESERVADO -> MachineReserved to "Reservado"
        MachineState.EN_USO -> MachineInUse to "En Uso"
        MachineState.GRACIA -> MachineGrace to "Gracia"
        MachineState.MANTENIMIENTO -> MachineMaintenance to "Mantenimiento"
        MachineState.FUERA_DE_SERVICIO -> MachineOutOfService to "Fuera de Servicio"
        MachineState.BLOQUEADA -> MachineMaintenance to "Bloqueada"
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun MachineStatusChipPreview() {
    GestorTurnosLaundryTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MachineStatusChip(status = MachineState.LIBRE)
            MachineStatusChip(status = MachineState.RESERVADO)
            MachineStatusChip(status = MachineState.EN_USO)
        }
    }
}