package ir.tadbir.app.ui.features.matrix

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.domain.model.Task
import ir.tadbir.app.ui.theme.AmberGold
import ir.tadbir.app.ui.theme.CyanAccent
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.RoseRed

@Composable
fun MatrixScreen(
    state: MatrixUiState,
    onIntent: (MatrixIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "ماتریس آیزنهاور",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "اولویت‌بندی کارهای باز بر اساس فوریت و اهمیت",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        // Q1: Urgent & Important
        item {
            QuadrantSection(
                title = "🔥 انجام فوری (مهم و فوری)",
                accentColor = RoseRed,
                tasks = state.q1DoFirst,
                onToggle = { onIntent(MatrixIntent.ToggleTask(it)) }
            )
        }

        // Q2: Important, Not Urgent
        item {
            QuadrantSection(
                title = "⭐ برنامه‌ریزی استراتژیک (مهم، غیر فوری)",
                accentColor = AmberGold,
                tasks = state.q2Schedule,
                onToggle = { onIntent(MatrixIntent.ToggleTask(it)) }
            )
        }

        // Q3: Urgent, Not Important
        item {
            QuadrantSection(
                title = "⚡ واگذاری یا تسریع (فوری، غیر مهم)",
                accentColor = CyanAccent,
                tasks = state.q3Delegate,
                onToggle = { onIntent(MatrixIntent.ToggleTask(it)) }
            )
        }

        // Q4: Not Urgent, Not Important
        item {
            QuadrantSection(
                title = "🗑️ حذف یا بازبینی (غیر فوری و غیر مهم)",
                accentColor = Color.Gray,
                tasks = state.q4Eliminate,
                onToggle = { onIntent(MatrixIntent.ToggleTask(it)) }
            )
        }
    }
}

@Composable
private fun QuadrantSection(
    title: String,
    accentColor: Color,
    tasks: List<Task>,
    onToggle: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (tasks.isEmpty()) {
                Text(
                    text = "موردی در این ربع وجود ندارد.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                tasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = { onToggle(task.id) },
                            colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
