package ir.tadbir.app.ui.features.daily

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.Priority
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.domain.model.TaskCategory
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.AmberGold
import ir.tadbir.app.ui.theme.CyanAccent
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.RoseRed

@Composable
fun DailyScreen(
    state: DailyUiState,
    onIntent: (DailyIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(DailyIntent.OpenAddTaskModal) },
                containerColor = EmeraldPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = pageBottomInset())
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "افزودن کار")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = pageBottomInset()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Date & Gamification (XP, Level, Streak)
            item {
                DailyHeader(state = state)
            }

            // Progress Summary Card
            item {
                DailyProgressCard(state = state)
            }

            // Frog of the Day (Eat The Frog)
            state.frogTask?.let { frog ->
                item {
                    FrogCard(task = frog, onToggle = { onIntent(DailyIntent.ToggleTask(frog.id)) })
                }
            }

            // Timed Tasks Section
            item {
                Text(
                    text = "⏰ زمان‌بندی روزانه",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (state.timedTasks.isEmpty()) {
                item {
                    EmptySectionCard("کار زمان‌داری برای امروز ثبت نشده است.")
                }
            } else {
                items(state.timedTasks, key = { it.id }) { task ->
                    TaskCardItem(
                        task = task,
                        onToggle = { onIntent(DailyIntent.ToggleTask(task.id)) },
                        onDelete = { onIntent(DailyIntent.DeleteTask(task.id)) }
                    )
                }
            }

            // Floating Tasks Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📝 کارهای شناور امروز",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (state.floatingTasks.isEmpty()) {
                item {
                    EmptySectionCard("کار شناوری ثبت نشده است.")
                }
            } else {
                items(state.floatingTasks, key = { it.id }) { task ->
                    TaskCardItem(
                        task = task,
                        onToggle = { onIntent(DailyIntent.ToggleTask(task.id)) },
                        onDelete = { onIntent(DailyIntent.DeleteTask(task.id)) }
                    )
                }
            }
        }
    }

    if (state.isAddTaskModalOpen) {
        AddTaskDialog(
            onDismiss = { onIntent(DailyIntent.CloseAddTaskModal) },
            onConfirm = { title, time, dur, prio, frog, imp, urg, cat ->
                onIntent(
                    DailyIntent.CreateTask(
                        title = title,
                        startTime = time,
                        durationMin = dur,
                        priority = prio,
                        isFrog = frog,
                        isImportant = imp,
                        isUrgent = urg,
                        category = cat
                    )
                )
            }
        )
    }
}

@Composable
private fun DailyHeader(state: DailyUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = state.selectedDate.formatted(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "امروز پرانرژی و متمرکز باش ✨",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Gamification Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Streak Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AmberGold.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.LocalFireDepartment,
                        contentDescription = "استریک",
                        tint = AmberGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${JalaliDate.toFa(state.streak)} روز",
                        color = AmberGold,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Level Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EmeraldPrimary.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.EmojiEvents,
                        contentDescription = "سطح",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "سطح ${JalaliDate.toFa(state.level)}",
                        color = EmeraldPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyProgressCard(state: DailyUiState) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "پیشرفت کارهای روز",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${JalaliDate.toFa(state.completedTasksCount)} از ${JalaliDate.toFa(state.totalTasksCount)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { state.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = EmeraldPrimary,
                trackColor = EmeraldPrimary.copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
private fun FrogCard(task: Task, onToggle: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1E3A2F),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🐸", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "قورباغه روز (مهم‌ترین هدف)",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
            )
        }
    }
}

@Composable
private fun TaskCardItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    task.startTime?.let { time ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyanAccent.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "⏰ $time (${JalaliDate.toFa(task.durationMin)}د)",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(task.category.colorHex).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = task.category.titleFa,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(task.category.colorHex),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (task.isImportant) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AmberGold.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "مهم",
                                style = MaterialTheme.typography.labelSmall,
                                color = AmberGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptySectionCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.03f))
    ) {
        Box(
            modifier = Modifier.padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        startTime: String?,
        durationMin: Int,
        priority: Priority,
        isFrog: Boolean,
        isImportant: Boolean,
        isUrgent: Boolean,
        category: TaskCategory
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var isFrog by remember { mutableStateOf(false) }
    var isImportant by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(TaskCategory.WORK) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "کار جدید در تدبیر",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان کار...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("ساعت شروع (اختیاری مثل 09:30)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isFrog, onCheckedChange = { isFrog = it })
                    Text("قورباغه روز است 🐸")
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isImportant, onCheckedChange = { isImportant = it })
                        Text("مهم")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isUrgent, onCheckedChange = { isUrgent = it })
                        Text("فوری")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            title,
                            if (startTime.isBlank()) null else startTime,
                            30,
                            Priority.MEDIUM,
                            isFrog,
                            isImportant,
                            isUrgent,
                            selectedCategory
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("افزودن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}
