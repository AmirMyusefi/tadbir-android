package ir.tadbir.app.ui.features.wellness

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.domain.model.MoodEntry

@Immutable
data class WellnessUiState(
    val todayMood: MoodEntry? = null,
    val badHabits: List<BadHabit> = emptyList(),
    val countdowns: List<CountdownEvent> = emptyList(),
    val showHabitDialog: Boolean = false,
    val showCountdownDialog: Boolean = false,

    // Habit form
    val formBadTitle: String = "",
    val formGoodTitle: String = "",

    // Countdown form
    val formCountdownTitle: String = "",
    val formCountdownDate: String = "",
    val formCountdownIcon: String = "📅"
) : ViewState

sealed interface WellnessIntent : ViewIntent {
    data object LoadData : WellnessIntent
    data class SetTodayMood(val mood: Int, val note: String = "") : WellnessIntent

    // Habit
    data object ShowHabitDialog : WellnessIntent
    data object DismissHabitDialog : WellnessIntent
    data class SetFormBadTitle(val v: String) : WellnessIntent
    data class SetFormGoodTitle(val v: String) : WellnessIntent
    data object SaveHabit : WellnessIntent
    data class DeleteHabit(val id: String) : WellnessIntent
    data class ToggleCleanToday(val habitId: String) : WellnessIntent

    // Countdown
    data object ShowCountdownDialog : WellnessIntent
    data object DismissCountdownDialog : WellnessIntent
    data class SetFormCountdownTitle(val v: String) : WellnessIntent
    data class SetFormCountdownDate(val v: String) : WellnessIntent
    data object SaveCountdown : WellnessIntent
    data class DeleteCountdown(val id: String) : WellnessIntent
}

sealed interface WellnessUiEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String) : WellnessUiEffect
}
