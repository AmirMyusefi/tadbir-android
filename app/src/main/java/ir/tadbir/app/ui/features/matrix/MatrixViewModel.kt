package ir.tadbir.app.ui.features.matrix

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatrixViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<MatrixIntent, MatrixUiState, MatrixUiEffect>() {

    override fun setInitialState(): MatrixUiState = MatrixUiState()

    init {
        repository.getAllTasks()
            .onEach { tasks ->
                val openTasks = tasks.filter { !it.isCompleted }
                val q1 = openTasks.filter { it.isUrgent && it.isImportant }
                val q2 = openTasks.filter { !it.isUrgent && it.isImportant }
                val q3 = openTasks.filter { it.isUrgent && !it.isImportant }
                val q4 = openTasks.filter { !it.isUrgent && !it.isImportant }

                setState {
                    copy(
                        q1DoFirst = q1,
                        q2Schedule = q2,
                        q3Delegate = q3,
                        q4Eliminate = q4
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntents(intent: MatrixIntent) {
        when (intent) {
            is MatrixIntent.ToggleTask -> {
                viewModelScope.launch {
                    repository.toggleTask(intent.taskId)
                }
            }
        }
    }
}
