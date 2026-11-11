package ir.tadbir.app.ui.features.goals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.domain.model.Goal
import ir.tadbir.app.domain.model.GoalCategory
import ir.tadbir.app.domain.model.GoalTimeframe
import ir.tadbir.app.domain.model.Milestone
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    state: GoalsUiState,
    onIntent: (GoalsIntent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            item {
                Spacer(modifier = Modifier.height(Spacing.xs))
                // Goals Summary Banner
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = EmeraldPrimary.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎯", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "اهداف بلندمدت و استراتژیک",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "شکستن اهداف سالانه به گام‌ها و مایلستون‌های قابل دستیابی",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            if (state.goals.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎯", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "هنوز هدفی ثبت نشده است",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "دکمه + پایین را برای تعریف اولین هدف لمس کنید",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                items(state.goals, key = { it.id }) { goal ->
                    GoalCard(
                        goal = goal,
                        onClick = { onIntent(GoalsIntent.ShowDetail(goal)) },
                        onDelete = { onIntent(GoalsIntent.DeleteGoal(goal.id)) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(pageBottomInset()))
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { onIntent(GoalsIntent.ShowAddSheet) },
            containerColor = EmeraldPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = pageBottomInset() + 16.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "هدف جدید")
        }

        // Add Goal Sheet
        if (state.showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = { onIntent(GoalsIntent.DismissSheet) },
                sheetState = bottomSheetState
            ) {
                AddGoalSheetContent(
                    state = state,
                    onIntent = onIntent
                )
            }
        }

        // Goal Detail Sheet
        state.showDetailFor?.let { goal ->
            ModalBottomSheet(
                onDismissRequest = { onIntent(GoalsIntent.DismissDetail) },
                sheetState = bottomSheetState
            ) {
                GoalDetailSheetContent(
                    goal = goal,
                    onToggleMilestone = { mId ->
                        onIntent(GoalsIntent.ToggleMilestone(goal.id, mId))
                    },
                    onAddMilestone = { title ->
                        onIntent(GoalsIntent.AddMilestone(goal.id, title))
                    },
                    onDelete = {
                        onIntent(GoalsIntent.DeleteGoal(goal.id))
                        onIntent(GoalsIntent.DismissDetail)
                    }
                )
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val progress by animateFloatAsState(targetValue = goal.progressFraction, label = "p")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(goal.icon, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${goal.category.emoji} ${goal.category.titleFa}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = " • ${goal.timeframe.titleFa}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Rounded.DeleteOutline,
                        contentDescription = "حذف",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (goal.vision.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = goal.vision,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "پیشرفت مایلستون‌ها (${goal.milestoneDoneCount}/${goal.milestones.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = "${(goal.progressFraction * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = EmeraldPrimary,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun AddGoalSheetContent(
    state: GoalsUiState,
    onIntent: (GoalsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "هدف جدید",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.formTitle,
            onValueChange = { onIntent(GoalsIntent.SetFormTitle(it)) },
            label = { Text("عنوان هدف") },
            placeholder = { Text("مثلاً: تسلط بر معماری Clean و Compose") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.formVision,
            onValueChange = { onIntent(GoalsIntent.SetFormVision(it)) },
            label = { Text("چشم‌انداز و چرایی (Vision)") },
            placeholder = { Text("چرا رسیدن به این هدف مهم است؟") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Timeframe selector
        Text("افق زمانی:", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GoalTimeframe.entries.forEach { tf ->
                val selected = tf == state.formTimeframe
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onIntent(GoalsIntent.SetFormTimeframe(tf)) }
                ) {
                    Text(
                        text = tf.titleFa,
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category selector
        Text("دسته‌بندی:", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            GoalCategory.entries.take(4).forEach { cat ->
                val selected = cat == state.formCategory
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onIntent(GoalsIntent.SetFormCategory(cat)) }
                ) {
                    Text(
                        text = "${cat.emoji} ${cat.titleFa}",
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onIntent(GoalsIntent.SaveGoal) },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ذخیره هدف", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun GoalDetailSheetContent(
    goal: Goal,
    onToggleMilestone: (String) -> Unit,
    onAddMilestone: (String) -> Unit,
    onDelete: () -> Unit
) {
    var newMilestoneText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(goal.icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${goal.category.emoji} ${goal.category.titleFa} • افق: ${goal.timeframe.titleFa}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.DeleteOutline, contentDescription = "حذف هدف", tint = MaterialTheme.colorScheme.error)
            }
        }

        if (goal.vision.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "چشم‌انداز: ${goal.vision}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "گام‌ها و مایلستون‌ها",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Milestones list
        goal.milestones.forEach { m ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleMilestone(m.id) }
                    .padding(vertical = 6.dp)
            ) {
                Icon(
                    imageVector = if (m.done) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (m.done) EmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = m.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (m.done) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (m.done) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
            }
        }

        // Add milestone input
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = newMilestoneText,
                onValueChange = { newMilestoneText = it },
                placeholder = { Text("مایلستون جدید...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newMilestoneText.isNotBlank()) {
                        onAddMilestone(newMilestoneText)
                        newMilestoneText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("افزودن")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
