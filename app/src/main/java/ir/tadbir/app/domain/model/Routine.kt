package ir.tadbir.app.domain.model

data class Routine(
    val id: String,
    val title: String,
    val daysOfWeek: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6), // 0 = Sat, 6 = Fri
    val targetTime: String? = null,
    val icon: String = "repeat",
    val streak: Int = 0,
    val isCompletedToday: Boolean = false
)
