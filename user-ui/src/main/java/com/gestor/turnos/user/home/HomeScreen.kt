package com.gestor.turnos.user.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.user.components.MachineCard
import com.gestor.turnos.user.components.NextBookingCard
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCalendar: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            viewModel.refresh()
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Laundry Manager",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCalendar,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = { Text("Reservar") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NextBookingCard(
                        booking = uiState.nextBooking,
                        onStartBooking = { viewModel.startBooking() },
                        onCancelBooking = { viewModel.cancelBooking() }
                    )
                }

                item {
                    Text(
                        text = "Estado de Máquinas",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (uiState.isLoading && uiState.machines.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(
                        items = uiState.machines,
                        key = { it.id }
                    ) { machine ->
                        MachineCard(
                            machine = machine,
                            onClick = { viewModel.onMachineClick(machine) }
                        )
                    }
                }

                if (uiState.machines.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay máquinas disponibles",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    GestorTurnosLaundryTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                nextBooking = Booking(
                    id = "1",
                    machineId = "machine1",
                    userId = "user1",
                    startDateTime = LocalDateTime.now().plusHours(2),
                    endDateTime = LocalDateTime.now().plusHours(4),
                    status = BookingStatus.RESERVADO
                ),
                machines = listOf(
                    Machine(
                        id = "1",
                        roomId = "room1",
                        name = "Lavarropas 1",
                        type = MachineType.LAVARROPAS,
                        status = MachineState.LIBRE
                    ),
                    Machine(
                        id = "2",
                        roomId = "room1",
                        name = "Lavarropas 2",
                        type = MachineType.LAVARROPAS,
                        status = MachineState.EN_USO
                    ),
                    Machine(
                        id = "3",
                        roomId = "room1",
                        name = "Secarropas 1",
                        type = MachineType.SECARROPAS,
                        status = MachineState.RESERVADO
                    )
                )
            ),
            onStartBooking = {},
            onCancelBooking = {},
            onMachineClick = {},
            onRefresh = {}
        )
    }
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onStartBooking: () -> Unit,
    onCancelBooking: () -> Unit,
    onMachineClick: (Machine) -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NextBookingCard(
                booking = uiState.nextBooking,
                onStartBooking = onStartBooking,
                onCancelBooking = onCancelBooking
            )
        }

        item {
            Text(
                text = "Estado de Máquinas",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(
            items = uiState.machines,
            key = { it.id }
        ) { machine ->
            MachineCard(
                machine = machine,
                onClick = { onMachineClick(machine) }
            )
        }
    }
}