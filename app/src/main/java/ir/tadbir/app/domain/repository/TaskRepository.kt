package ir.tadbir.app.domain.repository

import ir.tadbir.app.domain.model.InboxItem
import ir.tadbir.app.domain.model.Routine
import ir.tadbir.app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    // Tasks
    fun getTasksForDate(dateKey: String): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: String): Task?
    suspend fun insertOrUpdateTask(task: Task)
    suspend fun deleteTask(id: String)
    suspend fun toggleTask(id: String)

    // Routines
    fun getRoutines(): Flow<List<Routine>>
    suspend fun insertOrUpdateRoutine(routine: Routine)
    suspend fun deleteRoutine(id: String)
    suspend fun toggleRoutineToday(id: String)

    // Inbox (Brain Dump)
    fun getInboxItems(): Flow<List<InboxItem>>
    suspend fun insertInboxItem(item: InboxItem)
    suspend fun deleteInboxItem(id: String)
    suspend fun convertInboxItemToTask(itemId: String, dateKey: String, startTime: String?)
}
