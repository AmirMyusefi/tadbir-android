package ir.tadbir.app.domain.model

enum class Priority {
    HIGH, MEDIUM, LOW
}

enum class EnergyLevel {
    HIGH, MEDIUM, LOW
}

enum class TaskCategory(val titleFa: String, val colorHex: Long) {
    WORK("کار و شغل", 0xFF3B82F6),
    STUDY("مطالعه و یادگیری", 0xFF8B5CF6),
    PERSONAL("شخصی", 0xFFEC4899),
    HEALTH("سلامتی و ورزش", 0xFF10B981),
    FINANCE("مالی", 0xFFF59E0B),
    SPIRITUAL("روحی و ذهنی", 0xFF6366F1),
    SOCIAL("روابط اجتماعی", 0xFF14B8A6)
}

data class Task(
    val id: String,
    val title: String,
    val dateKey: String,
    val startTime: String? = null,
    val durationMin: Int = 30,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val isImportant: Boolean = false,
    val isUrgent: Boolean = false,
    val isFrog: Boolean = false,
    val energy: EnergyLevel = EnergyLevel.MEDIUM,
    val category: TaskCategory = TaskCategory.WORK,
    val notes: String = "",
    val rolloverCount: Int = 0
) {
    val isFloating: Boolean get() = startTime.isNullOrBlank()

    /**
     * Tadbir's priority algorithm matching PROJECT_BIBLE weights:
     * important +28, urgent +20, priority high 16 / medium 8, frog +10
     */
    val priorityScore: Int
        get() {
            var score = 0
            if (isImportant) score += 28
            if (isUrgent) score += 20
            score += when (priority) {
                Priority.HIGH -> 16
                Priority.MEDIUM -> 8
                Priority.LOW -> 0
            }
            if (isFrog) score += 10
            return score.coerceAtMost(100)
        }
}
