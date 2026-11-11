package ir.tadbir.app.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE dateKey = :dateKey ORDER BY startTime ASC, priority DESC")
    fun getTasksForDate(dateKey: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY dateKey DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String): Int

    @Query("UPDATE tasks SET isCompleted = NOT isCompleted WHERE id = :id")
    suspend fun toggleTask(id: String): Int
}

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines")
    fun getRoutines(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(routine: RoutineEntity)

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun deleteRoutine(id: String): Int

    @Query("SELECT * FROM routines WHERE id = :id LIMIT 1")
    suspend fun getRoutineById(id: String): RoutineEntity?
}

@Dao
interface InboxDao {
    @Query("SELECT * FROM inbox_items ORDER BY createdAt DESC")
    fun getInboxItems(): Flow<List<InboxItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: InboxItemEntity)

    @Query("DELETE FROM inbox_items WHERE id = :id")
    suspend fun deleteItem(id: String): Int
}

@Database(
    entities = [TaskEntity::class, RoutineEntity::class, InboxItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TadbirDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun routineDao(): RoutineDao
    abstract fun inboxDao(): InboxDao
}
