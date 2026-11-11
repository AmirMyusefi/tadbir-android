package ir.tadbir.app.ui.features.analytics

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState

@Immutable
data class AnalyticsUiState(
    val totalTasksCompleted: Int = 42,
    val bestStreakDays: Int = 18,
    val focusHoursTotal: Float = 24.5f,
    val xpTotal: Int = 580,
    val level: Int = 4,
    val weeklyCompletionRate: List<Float> = listOf(0.8f, 0.65f, 0.9f, 0.75f, 1.0f, 0.85f, 0.7f) // Sat..Fri
) : ViewState

sealed interface AnalyticsIntent : ViewIntent

sealed interface AnalyticsUiEffect : ViewSideEffect
