package com.gestor.turnos.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.repository.BookingRepository
import com.gestor.turnos.domain.repository.MachineRepository
import com.gestor.turnos.domain.usecase.CancelBookingUseCase
import com.gestor.turnos.domain.usecase.StartBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val nextBooking: Booking? = null,
    val machines: List<Machine> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val machineRepository: MachineRepository,
    private val startBookingUseCase: StartBookingUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // TODO: Get current user ID from authentication
    private val currentUserId = "current_user_id"

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            combine(
                bookingRepository.getBookingsByUser(currentUserId),
                machineRepository.getAllActiveMachines()
            ) { bookingsResult, machinesResult ->
                when {
                    bookingsResult is Result.Success && machinesResult is Result.Success -> {
                        val nextBooking = bookingsResult.data
                            .filter { booking ->
                                booking.status == BookingStatus.RESERVADO &&
                                booking.startDateTime.isAfter(LocalDateTime.now().minusHours(1))
                            }
                            .minByOrNull { it.startDateTime }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                nextBooking = nextBooking,
                                machines = machinesResult.data,
                                errorMessage = null
                            )
                        }
                    }
                    bookingsResult is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Error al cargar reservas: ${bookingsResult.exception.message}"
                            )
                        }
                    }
                    machinesResult is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Error al cargar máquinas: ${machinesResult.exception.message}"
                            )
                        }
                    }
                }
            }.collect { /* Data is handled in the combine block */ }
        }
    }

    fun startBooking() {
        val booking = _uiState.value.nextBooking ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = startBookingUseCase.execute(booking.id, booking.version)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nextBooking = result.data,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al iniciar turno: ${result.exception.message}"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun cancelBooking() {
        val booking = _uiState.value.nextBooking ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = cancelBookingUseCase.execute(booking.id, booking.version)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nextBooking = null,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al cancelar turno: ${result.exception.message}"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun onMachineClick(machine: Machine) {
        // TODO: Navigate to machine detail or calendar for this machine
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun refresh() {
        loadData()
    }
}