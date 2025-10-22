package com.gestor.turnos.user.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestor.turnos.core.constants.LaundryConstants
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.repository.BookingRepository
import com.gestor.turnos.domain.repository.MachineRepository
import com.gestor.turnos.domain.usecase.CreateBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

data class CalendarUiState(
    val isLoading: Boolean = false,
    val currentWeek: LocalDate = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1),
    val selectedMachine: Machine? = null,
    val machines: List<Machine> = emptyList(),
    val dayColumns: List<DayColumn> = emptyList(),
    val selectedSlots: Set<TimeSlot> = emptySet(),
    val isValidSelection: Boolean = false,
    val errorMessage: String? = null,
    val isBookingDialogOpen: Boolean = false
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val machineRepository: MachineRepository,
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // TODO: Get current user ID from authentication
    private val currentUserId = "current_user_id"

    init {
        loadMachines()
    }

    private fun loadMachines() {
        viewModelScope.launch {
            machineRepository.getAllActiveMachines().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                machines = result.data,
                                selectedMachine = result.data.firstOrNull()
                            )
                        }
                        // Load availability for the first machine
                        result.data.firstOrNull()?.let { machine ->
                            loadAvailability(machine.id)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = "Error al cargar máquinas: ${result.exception.message}")
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun selectMachine(machine: Machine) {
        _uiState.update {
            it.copy(
                selectedMachine = machine,
                selectedSlots = emptySet(),
                isValidSelection = false
            )
        }
        loadAvailability(machine.id)
    }

    fun navigateToWeek(weekOffset: Int) {
        val newWeek = _uiState.value.currentWeek.plusWeeks(weekOffset.toLong())
        _uiState.update {
            it.copy(
                currentWeek = newWeek,
                selectedSlots = emptySet(),
                isValidSelection = false
            )
        }
        _uiState.value.selectedMachine?.let { machine ->
            loadAvailability(machine.id)
        }
    }

    fun navigateToToday() {
        val today = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
        _uiState.update {
            it.copy(
                currentWeek = today,
                selectedSlots = emptySet(),
                isValidSelection = false
            )
        }
        _uiState.value.selectedMachine?.let { machine ->
            loadAvailability(machine.id)
        }
    }

    fun onSlotClick(slot: TimeSlot, slotIndex: Int) {
        if (!slot.isAvailable) return

        val currentSelection = _uiState.value.selectedSlots.toMutableSet()

        if (currentSelection.contains(slot)) {
            // Deselect slot
            currentSelection.remove(slot)
        } else {
            // Select slot - check if it's consecutive with existing selection
            if (currentSelection.isEmpty() || isConsecutiveSelection(currentSelection, slot)) {
                currentSelection.add(slot)
            } else {
                // Start new selection
                currentSelection.clear()
                currentSelection.add(slot)
            }
        }

        val isValid = CalendarUtils.isValidSlotSelection(currentSelection.toList()) &&
                CalendarUtils.areConsecutiveSlots(currentSelection.toList())

        _uiState.update {
            it.copy(
                selectedSlots = currentSelection,
                isValidSelection = isValid
            )
        }
    }

    fun clearSelection() {
        _uiState.update {
            it.copy(
                selectedSlots = emptySet(),
                isValidSelection = false
            )
        }
    }

    fun openBookingDialog() {
        if (_uiState.value.isValidSelection) {
            _uiState.update { it.copy(isBookingDialogOpen = true) }
        }
    }

    fun closeBookingDialog() {
        _uiState.update { it.copy(isBookingDialogOpen = false) }
    }

    fun confirmBooking() {
        val state = _uiState.value
        val machine = state.selectedMachine ?: return
        val selectedSlots = state.selectedSlots.toList().sortedBy { it.startTime }

        if (selectedSlots.isEmpty()) return

        val startDateTime = selectedSlots.first().startTime
        val endDateTime = selectedSlots.last().endTime

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = createBookingUseCase.execute(
                machineId = machine.id,
                userId = currentUserId,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedSlots = emptySet(),
                            isValidSelection = false,
                            isBookingDialogOpen = false,
                            errorMessage = null
                        )
                    }
                    // Reload availability
                    loadAvailability(machine.id)
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al crear reserva: ${result.exception.message}"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    private fun loadAvailability(machineId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Generate mock availability for now
            // TODO: Replace with actual API call
            val weekDays = CalendarUtils.generateWeekDays(_uiState.value.currentWeek)
            val dayColumns = weekDays.map { date ->
                DayColumn(
                    date = date,
                    slots = CalendarUtils.generateTimeSlots(date)
                )
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    dayColumns = dayColumns
                )
            }
        }
    }

    private fun isConsecutiveSelection(currentSelection: Set<TimeSlot>, newSlot: TimeSlot): Boolean {
        if (currentSelection.isEmpty()) return true

        val allSlots = (currentSelection + newSlot).sortedBy { it.startTime }
        return CalendarUtils.areConsecutiveSlots(allSlots)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}