package ir.tadbir.app.data.local

import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.InboxItem
import ir.tadbir.app.domain.model.Priority
import ir.tadbir.app.domain.model.Routine
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.domain.model.TaskCategory
import ir.tadbir.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val routineDao: RoutineDao,
    private val inboxDao: InboxDao
) : TaskRepository {

    override fun getTasksForDate(dateKey: String): Flow<List<Task>> {
        return taskDao.getTasksForDate(dateKey).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertOrUpdateTask(task: Task) {
        taskDao.insertOrUpdate(TaskEntity.fromDomain(task))
    }

    override suspend fun deleteTask(id: String) {
        taskDao.deleteTask(id)
    }

    override suspend fun toggleTask(id: String) {
        taskDao.toggleTask(id)
    }

    override fun getRoutines(): Flow<List<Routine>> {
        val todayKey = JalaliDate.today().key
        return routineDao.getRoutines().map { list ->
            list.map { entity ->
                val days = entity.daysOfWeek.split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
                Routine(
                    id = entity.id,
                    title = entity.title,
                    daysOfWeek = days,
                    targetTime = entity.targetTime,
                    icon = entity.icon,
                    streak = entity.streak,
                    isCompletedToday = entity.lastCompletedDate == todayKey
                )
            }
        }
    }

    override suspend fun insertOrUpdateRoutine(routine: Routine) {
        val entity = RoutineEntity(
            id = routine.id,
            title = routine.title,
            daysOfWeek = routine.daysOfWeek.joinToString(","),
            targetTime = routine.targetTime,
            icon = routine.icon,
            streak = routine.streak,
            lastCompletedDate = if (routine.isCompletedToday) JalaliDate.today().key else null
        )
        routineDao.insertOrUpdate(entity)
    }

    override suspend fun deleteRoutine(id: String) {
        routineDao.deleteRoutine(id)
    }

    override suspend fun toggleRoutineToday(id: String) {
        val routine = routineDao.getRoutineById(id) ?: return
        val todayKey = JalaliDate.today().key
        val isCompleted = routine.lastCompletedDate == todayKey
        val newStreak = if (isCompleted) (routine.streak - 1).coerceAtLeast(0) else routine.streak + 1
        val newDate = if (isCompleted) null else todayKey

        routineDao.insertOrUpdate(
            routine.copy(streak = newStreak, lastCompletedDate = newDate)
        )
    }

    override fun getInboxItems(): Flow<List<InboxItem>> {
        return inboxDao.getInboxItems().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertInboxItem(item: InboxItem) {
        inboxDao.insertOrUpdate(InboxItemEntity.fromDomain(item))
    }

    override suspend fun deleteInboxItem(id: String) {
        inboxDao.deleteItem(id)
    }

    override suspend fun convertInboxItemToTask(itemId: String, dateKey: String, startTime: String?) {
        val allItems = inboxDao.getInboxItems().firstOrNull() ?: emptyList()
        val item = allItems.firstOrNull { it.id == itemId } ?: return
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = item.text,
            dateKey = dateKey,
            startTime = startTime,
            durationMin = item.estimateMin,
            priority = Priority.MEDIUM,
            category = TaskCategory.PERSONAL
        )
        taskDao.insertOrUpdate(TaskEntity.fromDomain(task))
        inboxDao.deleteItem(itemId)
    }
}
