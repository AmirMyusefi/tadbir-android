package ir.tadbir.app.ui.features.goals

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.Goal
import ir.tadbir.app.domain.model.GoalCategory
import ir.tadbir.app.domain.model.GoalTimeframe

@Immutable
data class GoalsUiState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = false,
    val showAddSheet: Boolean = false,
    val showDetailFor: Goal? = null,
    // Add goal form
    val formTitle: String = "",
    val formIcon: String = "🎯",
    val formColor: String = "#4CAF50",
    val formVision: String = "",
    val formCategory: GoalCategory = GoalCategory.PERSONAL,
    val formTimeframe: GoalTimeframe = GoalTimeframe.ONE_YEAR,
    val formTarget: String = "0",
    val formReward: String = "",
    val editingGoalId: String? = null
) : ViewState

sealed interface GoalsIntent : ViewIntent {
    data object LoadGoals : GoalsIntent
    data object ShowAddSheet : GoalsIntent
    data object DismissSheet : GoalsIntent
    data class ShowDetail(val goal: Goal) : GoalsIntent
    data object DismissDetail : GoalsIntent

    // Form edits
    data class SetFormTitle(val v: String) : GoalsIntent
    data class SetFormIcon(val v: String) : GoalsIntent
    data class SetFormColor(val v: String) : GoalsIntent
    data class SetFormVision(val v: String) : GoalsIntent
    data class SetFormCategory(val v: GoalCategory) : GoalsIntent
    data class SetFormTimeframe(val v: GoalTimeframe) : GoalsIntent
    data class SetFormReward(val v: String) : GoalsIntent
    data class EditGoal(val goal: Goal) : GoalsIntent

    data object SaveGoal : GoalsIntent
    data class DeleteGoal(val id: String) : GoalsIntent
    data class ArchiveGoal(val id: String) : GoalsIntent
    data class ToggleMilestone(val goalId: String, val milestoneId: String) : GoalsIntent
    data class AddMilestone(val goalId: String, val title: String) : GoalsIntent
}

sealed interface GoalsUiEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String) : GoalsUiEffect
}
