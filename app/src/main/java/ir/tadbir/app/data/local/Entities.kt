package ir.tadbir.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.tadbir.app.domain.model.EnergyLevel
import ir.tadbir.app.domain.model.Priority
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
