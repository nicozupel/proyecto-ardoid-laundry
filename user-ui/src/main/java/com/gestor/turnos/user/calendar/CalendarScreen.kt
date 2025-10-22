package com.gestor.turnos.user.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestor.turnos.core.constants.LaundryConstants
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.user.calendar.components.WeeklyCalendarGrid
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calendario de Reservas",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (uiState.isValidSelection) {
                FloatingActionButton(
                    onClick = { viewModel.openBookingDialog() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirmar reserva"
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Machine selector
            MachineSelector(
                machines = uiState.machines,
                selectedMachine = uiState.selectedMachine,
                onMachineSelected = viewModel::selectMachine
            )

            // Week navigation
            WeekNavigationRow(
                currentWeek = uiState.currentWeek,
                onPreviousWeek = { viewModel.navigateToWeek(-1) },
                onNextWeek = { viewModel.navigateToWeek(1) },
                onToday = { viewModel.navigateToToday() }
            )

            // Selection info
            if (uiState.selectedSlots.isNotEmpty()) {
                SelectionInfo(
                    selectedSlots = uiState.selectedSlots,
                    isValid = uiState.isValidSelection,
                    onClear = { viewModel.clearSelection() }
                )
            }

            // Calendar grid
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                WeeklyCalendarGrid(
                    dayColumns = uiState.dayColumns,
                    selectedSlots = uiState.selectedSlots,
                    onSlotClick = viewModel::onSlotClick
                )
            }
        }
    }

    // Booking confirmation dialog
    if (uiState.isBookingDialogOpen) {
        BookingConfirmationDialog(
            selectedSlots = uiState.selectedSlots,
            machine = uiState.selectedMachine,
            onConfirm = { viewModel.confirmBooking() },
            onDismiss = { viewModel.closeBookingDialog() }
        )
    }
}

@Composable
private fun MachineSelector(
    machines: List<Machine>,
    selectedMachine: Machine?,
    onMachineSelected: (Machine) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(machines) { machine ->
            FilterChip(
                selected = machine.id == selectedMachine?.id,
                onClick = { onMachineSelected(machine) },
                label = { Text(machine.name) }
            )
        }
    }
}

@Composable
private fun WeekNavigationRow(
    currentWeek: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onToday: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Semana anterior"
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatWeekRange(currentWeek),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            TextButton(onClick = onToday) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null
                )
                Text("Hoy", modifier = Modifier.padding(start = 4.dp))
            }
        }

        IconButton(onClick = onNextWeek) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Semana siguiente"
            )
        }
    }
}

@Composable
private fun SelectionInfo(
    selectedSlots: Set<TimeSlot>,
    isValid: Boolean,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Seleccionados: ${selectedSlots.size} slots",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Duración: ${selectedSlots.size * LaundryConstants.Timing.GRID_GRANULARITY_MINUTES} min",
                style = MaterialTheme.typography.bodySmall,
                color = if (isValid) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        OutlinedButton(onClick = onClear) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
            Text("Limpiar", modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
private fun BookingConfirmationDialog(
    selectedSlots: Set<TimeSlot>,
    machine: Machine?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (selectedSlots.isEmpty() || machine == null) return

    val sortedSlots = selectedSlots.sortedBy { it.startTime }
    val startTime = sortedSlots.first().startTime
    val endTime = sortedSlots.last().endTime
    val duration = selectedSlots.size * LaundryConstants.Timing.GRID_GRANULARITY_MINUTES

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Reserva") },
        text = {
            Column {
                Text("Máquina: ${machine.name}")
                Text("Fecha: ${startTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}")
                Text("Horario: ${startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                Text("Duración: $duration minutos")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun formatWeekRange(startOfWeek: LocalDate): String {
    val endOfWeek = startOfWeek.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    return "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}"
}

@Preview
@Composable
fun CalendarScreenPreview() {
    GestorTurnosLaundryTheme {
        CalendarScreenContent(
            uiState = CalendarUiState(
                machines = listOf(
                    Machine("1", "room1", "Lavarropas 1", MachineType.LAVARROPAS, MachineState.LIBRE),
                    Machine("2", "room1", "Secarropas 1", MachineType.SECARROPAS, MachineState.LIBRE)
                ),
                selectedMachine = Machine("1", "room1", "Lavarropas 1", MachineType.LAVARROPAS, MachineState.LIBRE),
                dayColumns = CalendarUtils.generateWeekDays(LocalDate.now()).map { date ->
                    DayColumn(date, CalendarUtils.generateTimeSlots(date))
                }
            ),
            onMachineSelected = {},
            onSlotClick = { _, _ -> },
            onNavigateWeek = {},
            onToday = {},
            onClearSelection = {},
            onConfirmBooking = {}
        )
    }
}

@Composable
private fun CalendarScreenContent(
    uiState: CalendarUiState,
    onMachineSelected: (Machine) -> Unit,
    onSlotClick: (TimeSlot, Int) -> Unit,
    onNavigateWeek: (Int) -> Unit,
    onToday: () -> Unit,
    onClearSelection: () -> Unit,
    onConfirmBooking: () -> Unit
) {
    // Implementation would be similar to the main CalendarScreen
    // This is just for preview purposes
    Text("Calendar Screen Preview")
}