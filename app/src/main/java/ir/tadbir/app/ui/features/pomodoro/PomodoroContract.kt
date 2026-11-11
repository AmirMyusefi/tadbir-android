package ir.tadbir.app.ui.features.pomodoro

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState

enum class PomodoroMode(val titleFa: String, val defaultMinutes: Int) {
    FOCUS("تمرکز عمیق", 25),
    SHORT_BREAK("استراحت کوتاه", 5),
    LONG_BREAK("استراحت بلند", 15)
}

@Immutable
data class PomodoroUiState(
    val mode: PomodoroMode = PomodoroMode.FOCUS,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val completedSessionsCount: Int = 3,
    val currentTaskTitle: String? = "تمرکز روی برنامه تدبیر"
) : ViewState {
    val totalSeconds: Int get() = mode.defaultMinutes * 60
    val progressFraction: Float get() = 1f - (remainingSeconds.toFloat() / totalSeconds.toFloat())
    val formattedTime: String
        get() {
            val m = remainingSeconds / 60
            val s = remainingSeconds % 60
            return String.format("%02d:%02d", m, s)
        }
}

sealed interface PomodoroIntent : ViewIntent {
    data object ToggleTimer : PomodoroIntent
    data object ResetTimer : PomodoroIntent
    data class ChangeMode(val mode: PomodoroMode) : PomodoroIntent
    data object Tick : PomodoroIntent
}

sealed interface PomodoroUiEffect : ViewSideEffect {
    data class Notify(val message: String) : PomodoroUiEffect
}
