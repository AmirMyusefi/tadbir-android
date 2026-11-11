package ir.tadbir.app.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
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

    @Query("SELECT COUNT(*) FROM tasks WHERE dateKey = :dateKey AND isCompleted = 1")
    suspend fun countCompletedForDate(dateKey: String): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE dateKey = :dateKey")
    suspend fun countTotalForDate(dateKey: String): Int

    @Query("SELECT * FROM tasks WHERE dateKey BETWEEN :from AND :to ORDER BY dateKey ASC")
    fun getTasksInRange(from: String, to: String): Flow<List<TaskEntity>>
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

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE archived = 0 ORDER BY createdAt DESC")
    fun getGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: String): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoal(id: String): Int
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE archived = 0 ORDER BY createdAt DESC")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String): Int
}

@Dao
interface MoodDao {
    @Query("SELECT * FROM moods ORDER BY dateKey DESC")
    fun getMoods(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM moods WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getMoodForDate(dateKey: String): MoodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(mood: MoodEntity)

    @Query("SELECT * FROM moods WHERE dateKey BETWEEN :from AND :to ORDER BY dateKey ASC")
    fun getMoodsInRange(from: String, to: String): Flow<List<MoodEntity>>
}

@Dao
interface BadHabitDao {
    @Query("SELECT * FROM bad_habits ORDER BY createdAt DESC")
    fun getBadHabits(): Flow<List<BadHabitEntity>>

    @Query("SELECT * FROM bad_habits WHERE id = :id LIMIT 1")
    suspend fun getBadHabitById(id: String): BadHabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(habit: BadHabitEntity)

    @Query("DELETE FROM bad_habits WHERE id = :id")
    suspend fun deleteBadHabit(id: String): Int
}

@Dao
interface CountdownDao {
    @Query("SELECT * FROM countdowns ORDER BY dateKey ASC")
    fun getCountdowns(): Flow<List<CountdownEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(event: CountdownEntity)

    @Query("DELETE FROM countdowns WHERE id = :id")
    suspend fun deleteCountdown(id: String): Int
}

@Database(
    entities = [
        TaskEntity::class,
        RoutineEntity::class,
        InboxItemEntity::class,
        GoalEntity::class,
        ProjectEntity::class,
        MoodEntity::class,
        BadHabitEntity::class,
        CountdownEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class TadbirDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun routineDao(): RoutineDao
    abstract fun inboxDao(): InboxDao
    abstract fun goalDao(): GoalDao
    abstract fun projectDao(): ProjectDao
    abstract fun moodDao(): MoodDao
    abstract fun badHabitDao(): BadHabitDao
    abstract fun countdownDao(): CountdownDao
}
