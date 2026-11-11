package ir.tadbir.app.domain.model

data class Milestone(
    val id: String,
    val title: String,
    val done: Boolean = false
)

data class Goal(
    val id: String,
    val title: String,
    val icon: String = "🎯",
    val color: String = "#4CAF50",
    val target: Int = 0,               // Number of linked tasks to mark goal complete (0 = milestones only)
    val milestones: List<Milestone> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val vision: String = "",
    val category: GoalCategory = GoalCategory.PERSONAL,
    val reward: String = "",
    val difficulty: Int = 3,            // 1..5
    val timeframe: GoalTimeframe = GoalTimeframe.ONE_YEAR,
    val startDateKey: String = "",
    val endDateKey: String = "",
    val notes: String = "",
    val archived: Boolean = false
) {
    val milestoneDoneCount: Int get() = milestones.count { it.done }
    val progressFraction: Float
        get() = if (milestones.isEmpty()) 0f
        else milestoneDoneCount.toFloat() / milestones.size
}

enum class GoalCategory(val titleFa: String, val emoji: String) {
    PERSONAL("شخصی", "🧘"),
    HEALTH("سلامتی", "💪"),
    FINANCE("مالی", "💰"),
    CAREER("شغلی", "💼"),
    STUDY("یادگیری", "📚"),
    LEISURE("تفریح", "🎨"),
    RELATIONS("روابط", "❤️")
}

enum class GoalTimeframe(val titleFa: String) {
    THREE_MONTHS("۳ ماه"),
    SIX_MONTHS("۶ ماه"),
    ONE_YEAR("۱ سال"),
    FIVE_YEARS("۵ سال")
}
