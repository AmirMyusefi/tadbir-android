package ir.tadbir.app.domain.model

data class MoodEntry(
    val dateKey: String,
    val mood: Int,          // 1..5  (1=very bad … 5=excellent)
    val note: String = ""
)

data class CountdownEvent(
    val id: String,
    val title: String,
    val icon: String = "📅",
    val dateKey: String    // Jalali dateKey "1405-04-24"
)

data class BadHabit(
    val id: String,
    val badTitle: String,
    val goodTitle: String,
    val icon: String = "🚫",
    val createdAt: Long = System.currentTimeMillis(),
    /** Set of Jalali dateKeys for clean days */
    val cleanDays: Set<String> = emptySet()
) {
    val streak: Int
        get() = cleanDays.size  // simplified; real streak = consecutive from today

    fun currentStreak(todayKey: String): Int {
        // Count consecutive clean days ending at today
        // Keys are "1405-04-24" format, sort descending
        val sorted = cleanDays.sortedDescending()
        var streak = 0
        for (key in sorted) {
            if (streak == 0 && key == todayKey) { streak++; continue }
            if (streak == 0) break
            streak++
        }
        return streak
    }
}
