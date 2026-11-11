package ir.tadbir.app.domain.model

data class Project(
    val id: String,
    val title: String,
    val icon: String = "📁",
    val color: String = "#2196F3",
    val deadlineKey: String = "",
    val archived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val description: String = "",
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val priority: ProjectPriority = ProjectPriority.MEDIUM,
    val category: ProjectCategory = ProjectCategory.PERSONAL,
    val outcome: String = "",
    val notes: String = ""
)

enum class ProjectStatus(val titleFa: String, val emoji: String) {
    BACKLOG("انتظار", "🗂"),
    ACTIVE("فعال", "🔥"),
    PAUSED("مکث", "⏸"),
    DONE("تمام شده", "✅")
}

enum class ProjectPriority(val titleFa: String) {
    LOW("کم"), MEDIUM("متوسط"), HIGH("زیاد"), CRITICAL("بحرانی")
}

enum class ProjectCategory(val titleFa: String) {
    PERSONAL("شخصی"), WORK("کار"), STUDY("یادگیری"), TEAM("تیمی")
}
