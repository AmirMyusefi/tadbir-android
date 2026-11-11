package ir.tadbir.app.data.local

import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.domain.model.MoodEntry
import ir.tadbir.app.domain.repository.WellnessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WellnessRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao,
    private val badHabitDao: BadHabitDao,
    private val countdownDao: CountdownDao
) : WellnessRepository {

    // ---- Mood ----
    override fun getMoods(): Flow<List<MoodEntry>> =
        moodDao.getMoods().map { it.map { e -> e.toDomain() } }

    override suspend fun getMoodForDate(dateKey: String): MoodEntry? =
        moodDao.getMoodForDate(dateKey)?.toDomain()

    override suspend fun saveMood(entry: MoodEntry) =
        moodDao.insertOrUpdate(MoodEntity.fromDomain(entry))

    // ---- Bad Habits ----
    override fun getBadHabits(): Flow<List<BadHabit>> =
        badHabitDao.getBadHabits().map { it.map { e -> e.toDomain() } }

    override suspend fun insertOrUpdateBadHabit(habit: BadHabit) =
        badHabitDao.insertOrUpdate(BadHabitEntity.fromDomain(habit))

    override suspend fun deleteBadHabit(id: String) {
        badHabitDao.deleteBadHabit(id)
    }

    override suspend fun toggleCleanDay(id: String, dateKey: String) {
        val entity = badHabitDao.getBadHabitById(id) ?: return
        val current = entity.toDomain()
        val newDays = if (dateKey in current.cleanDays) {
            current.cleanDays - dateKey
        } else {
            current.cleanDays + dateKey
        }
        badHabitDao.insertOrUpdate(BadHabitEntity.fromDomain(current.copy(cleanDays = newDays)))
    }

    // ---- Countdown ----
    override fun getCountdowns(): Flow<List<CountdownEvent>> =
        countdownDao.getCountdowns().map { it.map { e -> e.toDomain() } }

    override suspend fun insertOrUpdateCountdown(event: CountdownEvent) =
        countdownDao.insertOrUpdate(CountdownEntity.fromDomain(event))

    override suspend fun deleteCountdown(id: String) {
        countdownDao.deleteCountdown(id)
    }
}
