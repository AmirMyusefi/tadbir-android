package ir.tadbir.app.ui.features.matrix

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.Task

@Immutable
data class MatrixUiState(
    val q1DoFirst: List<Task> = emptyList(),      // Urgent + Important (انجام فوری)
    val q2Schedule: List<Task> = emptyList(),    // Not Urgent + Important (برنامه‌ریزی)
    val q3Delegate: List<Task> = emptyList(),    // Urgent + Not Important (واگذاری)
    val q4Eliminate: List<Task> = emptyList()    // Not Urgent + Not Important (حذف)
) : ViewState

sealed interface MatrixIntent : ViewIntent {
    data class ToggleTask(val taskId: String) : MatrixIntent
}

sealed interface MatrixUiEffect : ViewSideEffect
