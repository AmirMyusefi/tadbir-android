package ir.tadbir.app.domain.repository

import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.domain.model.MoodEntry
import kotlinx.coroutines.flow.Flow

interface WellnessRepository {
    // Mood
    fun getMoods(): Flow<List<MoodEntry>>
    suspend fun getMoodForDate(dateKey: String): MoodEntry?
    suspend fun saveMood(entry: MoodEntry)

    // Bad Habits
    fun getBadHabits(): Flow<List<BadHabit>>
    suspend fun insertOrUpdateBadHabit(habit: BadHabit)
    suspend fun deleteBadHabit(id: String)
    suspend fun toggleCleanDay(id: String, dateKey: String)

    // Countdowns
    fun getCountdowns(): Flow<List<CountdownEvent>>
    suspend fun insertOrUpdateCountdown(event: CountdownEvent)
    suspend fun deleteCountdown(id: String)
}
