package ir.tadbir.app.domain.repository

import ir.tadbir.app.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    suspend fun getGoalById(id: String): Goal?
    suspend fun insertOrUpdateGoal(goal: Goal)
    suspend fun deleteGoal(id: String)
    suspend fun archiveGoal(id: String)
}
