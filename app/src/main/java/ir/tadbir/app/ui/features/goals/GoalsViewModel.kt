package ir.tadbir.app.ui.features.goals

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.model.Goal
import ir.tadbir.app.domain.model.Milestone
import ir.tadbir.app.domain.repository.GoalRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepo: GoalRepository
) : BaseViewModel<GoalsIntent, GoalsUiState, GoalsUiEffect>() {

    override fun setInitialState(): GoalsUiState = GoalsUiState()

    init {
        handleIntents(GoalsIntent.LoadGoals)
    }

    override fun handleIntents(intent: GoalsIntent) {
        when (intent) {
            is GoalsIntent.LoadGoals -> {
                goalRepo.getGoals()
                    .onEach { list -> setState { copy(goals = list, isLoading = false) } }
                    .launchIn(viewModelScope)
            }

            is GoalsIntent.ShowAddSheet ->
                setState {
                    copy(
                        showAddSheet = true,
                        editingGoalId = null,
                        formTitle = "",
                        formIcon = "🎯",
                        formColor = "#4CAF50",
                        formVision = "",
                        formReward = ""
                    )
                }

            is GoalsIntent.DismissSheet ->
                setState { copy(showAddSheet = false) }

            is GoalsIntent.ShowDetail ->
                setState { copy(showDetailFor = intent.goal) }

            is GoalsIntent.DismissDetail ->
                setState { copy(showDetailFor = null) }

            is GoalsIntent.SetFormTitle -> setState { copy(formTitle = intent.v) }
            is GoalsIntent.SetFormIcon -> setState { copy(formIcon = intent.v) }
            is GoalsIntent.SetFormColor -> setState { copy(formColor = intent.v) }
            is GoalsIntent.SetFormVision -> setState { copy(formVision = intent.v) }
            is GoalsIntent.SetFormCategory -> setState { copy(formCategory = intent.v) }
            is GoalsIntent.SetFormTimeframe -> setState { copy(formTimeframe = intent.v) }
            is GoalsIntent.SetFormReward -> setState { copy(formReward = intent.v) }

            is GoalsIntent.EditGoal -> {
                val g = intent.goal
                setState {
                    copy(
                        showAddSheet = true,
                        editingGoalId = g.id,
                        formTitle = g.title,
                        formIcon = g.icon,
                        formColor = g.color,
                        formVision = g.vision,
                        formCategory = g.category,
                        formTimeframe = g.timeframe,
                        formReward = g.reward
                    )
                }
            }

            is GoalsIntent.SaveGoal -> {
                val s = viewState.value
                if (s.formTitle.isBlank()) {
                    setEffect { GoalsUiEffect.ShowSnackbar("عنوان هدف را وارد کنید") }
                    return
                }
                viewModelScope.launch {
                    val existing = s.editingGoalId?.let { goalRepo.getGoalById(it) }
                    val goal = (existing ?: Goal(
                        id = UUID.randomUUID().toString(),
                        title = s.formTitle
                    )).copy(
                        title = s.formTitle,
                        icon = s.formIcon,
                        color = s.formColor,
                        vision = s.formVision,
                        category = s.formCategory,
                        timeframe = s.formTimeframe,
                        reward = s.formReward
                    )
                    goalRepo.insertOrUpdateGoal(goal)
                    setState { copy(showAddSheet = false) }
                }
            }

            is GoalsIntent.DeleteGoal -> {
                viewModelScope.launch { goalRepo.deleteGoal(intent.id) }
            }

            is GoalsIntent.ArchiveGoal -> {
                viewModelScope.launch { goalRepo.archiveGoal(intent.id) }
            }

            is GoalsIntent.ToggleMilestone -> {
                viewModelScope.launch {
                    val goal = goalRepo.getGoalById(intent.goalId) ?: return@launch
                    val newMilestones = goal.milestones.map { m ->
                        if (m.id == intent.milestoneId) m.copy(done = !m.done) else m
                    }
                    goalRepo.insertOrUpdateGoal(goal.copy(milestones = newMilestones))
                    setState { copy(showDetailFor = showDetailFor?.copy(milestones = newMilestones)) }
                }
            }

            is GoalsIntent.AddMilestone -> {
                viewModelScope.launch {
                    val goal = goalRepo.getGoalById(intent.goalId) ?: return@launch
                    val ms = Milestone(
                        id = UUID.randomUUID().toString(),
                        title = intent.title.trim(),
                        done = false
                    )
                    val updated = goal.copy(milestones = goal.milestones + ms)
                    goalRepo.insertOrUpdateGoal(updated)
                    setState { copy(showDetailFor = updated) }
                }
            }
        }
    }
}
