package ir.tadbir.app.ui.features.routines

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.model.Routine
import ir.tadbir.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoutinesViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<RoutinesIntent, RoutinesUiState, RoutinesUiEffect>() {

    override fun setInitialState(): RoutinesUiState = RoutinesUiState()

    init {
        repository.getRoutines()
            .onEach { list ->
                setState { copy(routines = list) }
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntents(intent: RoutinesIntent) {
        when (intent) {
            is RoutinesIntent.ToggleRoutine -> {
                viewModelScope.launch {
                    repository.toggleRoutineToday(intent.routineId)
                }
            }
            is RoutinesIntent.DeleteRoutine -> {
                viewModelScope.launch {
                    repository.deleteRoutine(intent.routineId)
                    setEffect { RoutinesUiEffect.ShowMessage("روتین حذف شد") }
                }
            }
            RoutinesIntent.OpenAddModal -> {
                setState { copy(isAddRoutineModalOpen = true) }
            }
            RoutinesIntent.CloseAddModal -> {
                setState { copy(isAddRoutineModalOpen = false) }
            }
            is RoutinesIntent.CreateRoutine -> {
                viewModelScope.launch {
                    val routine = Routine(
                        id = UUID.randomUUID().toString(),
                        title = intent.title,
                        targetTime = intent.targetTime
                    )
                    repository.insertOrUpdateRoutine(routine)
                    setState { copy(isAddRoutineModalOpen = false) }
                    setEffect { RoutinesUiEffect.ShowMessage("عادت جدید ثبت شد! استریک را بساز 🔥") }
                }
            }
        }
    }
}
