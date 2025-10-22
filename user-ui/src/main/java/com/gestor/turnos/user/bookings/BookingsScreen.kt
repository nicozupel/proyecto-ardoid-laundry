package com.gestor.turnos.user.bookings

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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.repository.BookingRepository
import com.gestor.turnos.domain.usecase.CancelBookingUseCase
import com.gestor.turnos.domain.usecase.StartBookingUseCase
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import com.gestor.turnos.user.components.BookingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class BookingsUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val selectedFilter: BookingFilter = BookingFilter.ALL,
    val errorMessage: String? = null
)

enum class BookingFilter(val displayName: String) {
    ALL("Todos"),
    UPCOMING("Próximos"),
    ACTIVE("Activos"),
    COMPLETED("Completados")
}

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val startBookingUseCase: StartBookingUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingsUiState())
    val uiState: StateFlow<BookingsUiState> = _uiState.asStateFlow()

    // TODO: Get current user ID from authentication
    private val currentUserId = "current_user_id"

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            bookingRepository.getBookingsByUser(currentUserId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                bookings = result.data.sortedByDescending { booking -> booking.startDateTime },
                                errorMessage = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Error al cargar reservas: ${result.exception.message}"
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun setFilter(filter: BookingFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun startBooking(booking: Booking) {
        viewModelScope.launch {
            when (val result = startBookingUseCase.execute(booking.id, booking.version)) {
                is Result.Success -> {
                    // Booking list will be updated automatically through the repository flow
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = "Error al iniciar turno: ${result.exception.message}")
                    }
                }
                is Result.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }

    fun cancelBooking(booking: Booking) {
        viewModelScope.launch {
            when (val result = cancelBookingUseCase.execute(booking.id, booking.version)) {
                is Result.Success -> {
                    // Booking list will be updated automatically through the repository flow
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = "Error al cancelar turno: ${result.exception.message}")
                    }
                }
                is Result.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun refresh() {
        loadBookings()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    viewModel: BookingsViewModel = hiltViewModel()
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
                        text = "Mis Turnos",
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            FilterRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::setFilter,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Bookings list
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredBookings = filterBookings(uiState.bookings, uiState.selectedFilter)

                if (filteredBookings.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (uiState.selectedFilter) {
                                BookingFilter.ALL -> "No tienes reservas"
                                BookingFilter.UPCOMING -> "No tienes próximos turnos"
                                BookingFilter.ACTIVE -> "No tienes turnos activos"
                                BookingFilter.COMPLETED -> "No tienes turnos completados"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredBookings,
                            key = { it.id }
                        ) { booking ->
                            BookingListItem(
                                booking = booking,
                                onStart = { viewModel.startBooking(booking) },
                                onEdit = { /* TODO: Navigate to edit */ },
                                onCancel = { viewModel.cancelBooking(booking) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selectedFilter: BookingFilter,
    onFilterSelected: (BookingFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(BookingFilter.entries) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.displayName) }
            )
        }
    }
}

private fun filterBookings(bookings: List<Booking>, filter: BookingFilter): List<Booking> {
    val now = LocalDateTime.now()

    return when (filter) {
        BookingFilter.ALL -> bookings
        BookingFilter.UPCOMING -> bookings.filter {
            it.status == BookingStatus.RESERVADO && it.startDateTime.isAfter(now)
        }
        BookingFilter.ACTIVE -> bookings.filter {
            it.status == BookingStatus.EN_USO ||
            (it.status == BookingStatus.RESERVADO && it.startDateTime.isBefore(now.plusMinutes(30)))
        }
        BookingFilter.COMPLETED -> bookings.filter {
            it.status == BookingStatus.COMPLETADO || it.status == BookingStatus.CANCELADO
        }
    }
}

@Preview
@Composable
fun BookingsScreenPreview() {
    GestorTurnosLaundryTheme {
        BookingsScreenContent(
            uiState = BookingsUiState(
                bookings = listOf(
                    Booking(
                        id = "1",
                        machineId = "Lavarropas 1",
                        userId = "user1",
                        startDateTime = LocalDateTime.now().plusHours(2),
                        endDateTime = LocalDateTime.now().plusHours(4),
                        status = BookingStatus.RESERVADO
                    ),
                    Booking(
                        id = "2",
                        machineId = "Secarropas 1",
                        userId = "user1",
                        startDateTime = LocalDateTime.now().minusHours(1),
                        endDateTime = LocalDateTime.now().plusHours(1),
                        status = BookingStatus.EN_USO
                    ),
                    Booking(
                        id = "3",
                        machineId = "Lavarropas 2",
                        userId = "user1",
                        startDateTime = LocalDateTime.now().minusDays(1),
                        endDateTime = LocalDateTime.now().minusDays(1).plusHours(2),
                        status = BookingStatus.COMPLETADO
                    )
                )
            ),
            onFilterSelected = {},
            onStartBooking = {},
            onCancelBooking = {},
            onRefresh = {}
        )
    }
}

@Composable
private fun BookingsScreenContent(
    uiState: BookingsUiState,
    onFilterSelected: (BookingFilter) -> Unit,
    onStartBooking: (Booking) -> Unit,
    onCancelBooking: (Booking) -> Unit,
    onRefresh: () -> Unit
) {
    // This is just for preview purposes - actual implementation is in the main BookingsScreen
    Text("Bookings Screen Preview")
}