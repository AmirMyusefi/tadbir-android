package ir.tadbir.app.ui.features.daily

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.model.Priority
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.domain.model.TaskCategory
import ir.tadbir.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<DailyIntent, DailyUiState, DailyUiEffect>() {

    override fun setInitialState(): DailyUiState = DailyUiState()

    init {
        observeTasks(viewState.value.selectedDate.key)
    }

    override fun handleIntents(intent: DailyIntent) {
        when (intent) {
            is DailyIntent.ChangeDate -> {
                setState { copy(selectedDate = intent.date) }
                observeTasks(intent.date.key)
            }
            is DailyIntent.ToggleTask -> {
                viewModelScope.launch {
                    repository.toggleTask(intent.taskId)
                }
            }
            is DailyIntent.DeleteTask -> {
                viewModelScope.launch {
                    repository.deleteTask(intent.taskId)
                    setEffect { DailyUiEffect.ShowToast("کار حذف شد") }
                }
            }
            DailyIntent.OpenAddTaskModal -> {
                setState { copy(isAddTaskModalOpen = true) }
            }
            DailyIntent.CloseAddTaskModal -> {
                setState { copy(isAddTaskModalOpen = false) }
            }
            is DailyIntent.CreateTask -> {
                viewModelScope.launch {
                    val task = Task(
                        id = UUID.randomUUID().toString(),
                        title = intent.title,
                        dateKey = viewState.value.selectedDate.key,
                        startTime = intent.startTime,
                        durationMin = intent.durationMin,
                        priority = intent.priority,
                        isFrog = intent.isFrog,
                        isImportant = intent.isImportant,
                        isUrgent = intent.isUrgent,
                        category = intent.category
                    )
                    repository.insertOrUpdateTask(task)
                    setState { copy(isAddTaskModalOpen = false) }
                    setEffect { DailyUiEffect.ShowToast("کار جدید با موفقیت اضافه شد") }
                }
            }
        }
    }

    private fun observeTasks(dateKey: String) {
        repository.getTasksForDate(dateKey)
            .onEach { tasks ->
                val timed = tasks.filter { !it.isFloating }
                val floating = tasks.filter { it.isFloating }
                val frog = tasks.firstOrNull { it.isFrog }

                setState {
                    copy(
                        timedTasks = timed,
                        floatingTasks = floating,
                        frogTask = frog
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
