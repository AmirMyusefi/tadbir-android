package ir.tadbir.app.ui.features.daily

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.Priority
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.domain.model.TaskCategory

@Immutable
data class DailyUiState(
    val selectedDate: JalaliDate = JalaliDate.today(),
    val timedTasks: List<Task> = emptyList(),
    val floatingTasks: List<Task> = emptyList(),
    val frogTask: Task? = null,
    val streak: Int = 5,
    val level: Int = 3,
    val xp: Int = 240,
    val isLoading: Boolean = false,
    val isAddTaskModalOpen: Boolean = false
) : ViewState {
    val totalTasksCount: Int get() = timedTasks.size + floatingTasks.size
    val completedTasksCount: Int get() = timedTasks.count { it.isCompleted } + floatingTasks.count { it.isCompleted }
    val progressFraction: Float get() = if (totalTasksCount == 0) 0f else completedTasksCount.toFloat() / totalTasksCount
}

sealed interface DailyIntent : ViewIntent {
    data class ChangeDate(val date: JalaliDate) : DailyIntent
    data class ToggleTask(val taskId: String) : DailyIntent
    data class DeleteTask(val taskId: String) : DailyIntent
    data object OpenAddTaskModal : DailyIntent
    data object CloseAddTaskModal : DailyIntent
    data class CreateTask(
        val title: String,
        val startTime: String?,
        val durationMin: Int,
        val priority: Priority,
        val isFrog: Boolean,
        val isImportant: Boolean,
        val isUrgent: Boolean,
        val category: TaskCategory
    ) : DailyIntent
}

sealed interface DailyUiEffect : ViewSideEffect {
    data class ShowToast(val message: String) : DailyUiEffect
    data class NavigateToTaskDetails(val taskId: String) : DailyUiEffect
}
