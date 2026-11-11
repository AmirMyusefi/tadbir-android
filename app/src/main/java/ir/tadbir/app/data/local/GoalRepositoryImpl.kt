package ir.tadbir.app.data.local

import ir.tadbir.app.domain.model.Goal
import ir.tadbir.app.domain.model.Milestone
import ir.tadbir.app.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getGoals(): Flow<List<Goal>> =
        goalDao.getGoals().map { it.map { e -> e.toDomain() } }

    override suspend fun getGoalById(id: String): Goal? =
        goalDao.getGoalById(id)?.toDomain()

    override suspend fun insertOrUpdateGoal(goal: Goal) =
        goalDao.insertOrUpdate(GoalEntity.fromDomain(goal))

    override suspend fun deleteGoal(id: String) {
        goalDao.deleteGoal(id)
    }

    override suspend fun archiveGoal(id: String) {
        val entity = goalDao.getGoalById(id) ?: return
        goalDao.insertOrUpdate(entity.copy(archived = true))
    }
}
