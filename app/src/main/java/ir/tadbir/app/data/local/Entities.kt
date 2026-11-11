package ir.tadbir.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.domain.model.EnergyLevel
import ir.tadbir.app.domain.model.Goal
import ir.tadbir.app.domain.model.GoalCategory
import ir.tadbir.app.domain.model.GoalTimeframe
import ir.tadbir.app.domain.model.InboxItem
import ir.tadbir.app.domain.model.Milestone
import ir.tadbir.app.domain.model.MoodEntry
import ir.tadbir.app.domain.model.Priority
import ir.tadbir.app.domain.model.Project
import ir.tadbir.app.domain.model.ProjectCategory
import ir.tadbir.app.domain.model.ProjectPriority
import ir.tadbir.app.domain.model.ProjectStatus
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.domain.model.TaskCategory

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val dateKey: String,
    val startTime: String?,
    val durationMin: Int,
    val isCompleted: Boolean,
    val priority: String,
    val isImportant: Boolean,
    val isUrgent: Boolean,
    val isFrog: Boolean,
    val energy: String,
    val category: String,
    val notes: String,
    val rolloverCount: Int
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        dateKey = dateKey,
        startTime = startTime,
        durationMin = durationMin,
        isCompleted = isCompleted,
        priority = Priority.valueOf(priority),
        isImportant = isImportant,
        isUrgent = isUrgent,
        isFrog = isFrog,
        energy = EnergyLevel.valueOf(energy),
        category = TaskCategory.valueOf(category),
        notes = notes,
        rolloverCount = rolloverCount
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            dateKey = task.dateKey,
            startTime = task.startTime,
            durationMin = task.durationMin,
            isCompleted = task.isCompleted,
            priority = task.priority.name,
            isImportant = task.isImportant,
            isUrgent = task.isUrgent,
            isFrog = task.isFrog,
            energy = task.energy.name,
            category = task.category.name,
            notes = task.notes,
            rolloverCount = task.rolloverCount
        )
    }
}

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey val id: String,
    val title: String,
    val daysOfWeek: String, // comma-separated e.g. "0,1,2,3,4,5,6"
    val targetTime: String?,
    val icon: String,
    val streak: Int,
    val lastCompletedDate: String?
)

@Entity(tableName = "inbox_items")
data class InboxItemEntity(
    @PrimaryKey val id: String,
    val text: String,
    val createdAt: Long,
    val estimateMin: Int,
    val energy: String
) {
    fun toDomain(): InboxItem = InboxItem(
        id = id,
        text = text,
        createdAt = createdAt,
        estimateMin = estimateMin,
        energy = EnergyLevel.valueOf(energy)
    )

    companion object {
        fun fromDomain(item: InboxItem): InboxItemEntity = InboxItemEntity(
            id = item.id,
            text = item.text,
            createdAt = item.createdAt,
            estimateMin = item.estimateMin,
            energy = item.energy.name
        )
    }
}

// ----- Goal Entity -----
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val icon: String,
    val color: String,
    val target: Int,
    /** JSON-serialized milestones: "id|title|done,id2|title2|done2" */
    val milestonesJson: String,
    val createdAt: Long,
    val vision: String,
    val category: String,
    val reward: String,
    val difficulty: Int,
    val timeframe: String,
    val startDateKey: String,
    val endDateKey: String,
    val notes: String,
    val archived: Boolean
) {
    fun toDomain(): Goal = Goal(
        id = id,
        title = title,
        icon = icon,
        color = color,
        target = target,
        milestones = parseMilestones(milestonesJson),
        createdAt = createdAt,
        vision = vision,
        category = runCatching { GoalCategory.valueOf(category) }.getOrDefault(GoalCategory.PERSONAL),
        reward = reward,
        difficulty = difficulty,
        timeframe = runCatching { GoalTimeframe.valueOf(timeframe) }.getOrDefault(GoalTimeframe.ONE_YEAR),
        startDateKey = startDateKey,
        endDateKey = endDateKey,
        notes = notes,
        archived = archived
    )

    companion object {
        fun fromDomain(g: Goal): GoalEntity = GoalEntity(
            id = g.id, title = g.title, icon = g.icon, color = g.color,
            target = g.target, milestonesJson = serializeMilestones(g.milestones),
            createdAt = g.createdAt, vision = g.vision,
            category = g.category.name, reward = g.reward,
            difficulty = g.difficulty, timeframe = g.timeframe.name,
            startDateKey = g.startDateKey, endDateKey = g.endDateKey,
            notes = g.notes, archived = g.archived
        )

        private fun serializeMilestones(list: List<Milestone>): String =
            list.joinToString(",") { "${it.id}|${it.title.replace("|", "\\|")}|${it.done}" }

        private fun parseMilestones(json: String): List<Milestone> {
            if (json.isBlank()) return emptyList()
            return json.split(",").mapNotNull { part ->
                val pieces = part.split("|")
                if (pieces.size >= 3) Milestone(
                    id = pieces[0],
                    title = pieces[1].replace("\\|", "|"),
                    done = pieces[2] == "true"
                ) else null
            }
        }
    }
}

// ----- Project Entity -----
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val icon: String,
    val color: String,
    val deadlineKey: String,
    val archived: Boolean,
    val createdAt: Long,
    val description: String,
    val status: String,
    val priority: String,
    val category: String,
    val outcome: String,
    val notes: String
) {
    fun toDomain(): Project = Project(
        id = id, title = title, icon = icon, color = color,
        deadlineKey = deadlineKey, archived = archived,
        createdAt = createdAt, description = description,
        status = runCatching { ProjectStatus.valueOf(status) }.getOrDefault(ProjectStatus.ACTIVE),
        priority = runCatching { ProjectPriority.valueOf(priority) }.getOrDefault(ProjectPriority.MEDIUM),
        category = runCatching { ProjectCategory.valueOf(category) }.getOrDefault(ProjectCategory.PERSONAL),
        outcome = outcome, notes = notes
    )

    companion object {
        fun fromDomain(p: Project): ProjectEntity = ProjectEntity(
            id = p.id, title = p.title, icon = p.icon, color = p.color,
            deadlineKey = p.deadlineKey, archived = p.archived,
            createdAt = p.createdAt, description = p.description,
            status = p.status.name, priority = p.priority.name,
            category = p.category.name, outcome = p.outcome, notes = p.notes
        )
    }
}

// ----- Mood Entity -----
@Entity(tableName = "moods", primaryKeys = ["dateKey"])
data class MoodEntity(
    val dateKey: String,
    val mood: Int,
    val note: String
) {
    fun toDomain() = MoodEntry(dateKey = dateKey, mood = mood, note = note)
    companion object {
        fun fromDomain(m: MoodEntry) = MoodEntity(dateKey = m.dateKey, mood = m.mood, note = m.note)
    }
}

// ----- BadHabit Entity -----
@Entity(tableName = "bad_habits")
data class BadHabitEntity(
    @PrimaryKey val id: String,
    val badTitle: String,
    val goodTitle: String,
    val icon: String,
    val createdAt: Long,
    /** comma-separated dateKeys of clean days */
    val cleanDaysJson: String
) {
    fun toDomain(): BadHabit = BadHabit(
        id = id, badTitle = badTitle, goodTitle = goodTitle, icon = icon,
        createdAt = createdAt,
        cleanDays = if (cleanDaysJson.isBlank()) emptySet()
        else cleanDaysJson.split(",").filter { it.isNotBlank() }.toSet()
    )

    companion object {
        fun fromDomain(h: BadHabit): BadHabitEntity = BadHabitEntity(
            id = h.id, badTitle = h.badTitle, goodTitle = h.goodTitle,
            icon = h.icon, createdAt = h.createdAt,
            cleanDaysJson = h.cleanDays.joinToString(",")
        )
    }
}

// ----- Countdown Entity -----
@Entity(tableName = "countdowns")
data class CountdownEntity(
    @PrimaryKey val id: String,
    val title: String,
    val icon: String,
    val dateKey: String
) {
    fun toDomain() = CountdownEvent(id = id, title = title, icon = icon, dateKey = dateKey)
    companion object {
        fun fromDomain(e: CountdownEvent) = CountdownEntity(id = e.id, title = e.title, icon = e.icon, dateKey = e.dateKey)
    }
}
