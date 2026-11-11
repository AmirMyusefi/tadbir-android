package ir.tadbir.app.ui.features.routines

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.Routine

@Immutable
data class RoutinesUiState(
    val routines: List<Routine> = emptyList(),
    val totalStreak: Int = 14,
    val isAddRoutineModalOpen: Boolean = false
) : ViewState

sealed interface RoutinesIntent : ViewIntent {
    data class ToggleRoutine(val routineId: String) : RoutinesIntent
    data class DeleteRoutine(val routineId: String) : RoutinesIntent
    data object OpenAddModal : RoutinesIntent
    data object CloseAddModal : RoutinesIntent
    data class CreateRoutine(val title: String, val targetTime: String?) : RoutinesIntent
}

sealed interface RoutinesUiEffect : ViewSideEffect {
    data class ShowMessage(val text: String) : RoutinesUiEffect
}
