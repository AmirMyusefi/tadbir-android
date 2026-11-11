package ir.tadbir.app.ui.features.wellness

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
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.Spacing

private val MOOD_OPTIONS = listOf(
    1 to "😫 داغون",
    2 to "😕 کلافه",
    3 to "😐 معمولی",
    4 to "🙂 خوب",
    5 to "🤩 پرانرژی"
)

@Composable
fun WellnessScreen(
    state: WellnessUiState,
    onIntent: (WellnessIntent) -> Unit
) {
    val todayKey = JalaliDate.today().key

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        item {
            Spacer(modifier = Modifier.height(Spacing.xs))
            // Section 1: Daily Mood Tracker
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "حال و روحیهٔ امروز شما چطور است؟",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MOOD_OPTIONS.forEach { (moodScore, label) ->
                            val isSelected = state.todayMood?.mood == moodScore
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .clickable { onIntent(WellnessIntent.SetTodayMood(moodScore)) }
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Bad Habits Replacement
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "ترک عادت‌های نامطلوب",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "جایگزینی عادت منفی با یک رفتار مثبت سازنده",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = { onIntent(WellnessIntent.ShowHabitDialog) }) {
                    Icon(Icons.Rounded.Add, contentDescription = "عادت جدید", tint = EmeraldPrimary)
                }
            }
        }

        if (state.badHabits.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "هنوز عادت منفی‌ای برای ردیابی ثبت نکرده‌اید. با دکمه + آغاز کنید.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(state.badHabits, key = { it.id }) { habit ->
                val isCleanToday = todayKey in habit.cleanDays
                BadHabitCard(
                    habit = habit,
                    isCleanToday = isCleanToday,
                    onToggleClean = { onIntent(WellnessIntent.ToggleCleanToday(habit.id)) },
                    onDelete = { onIntent(WellnessIntent.DeleteHabit(habit.id)) }
                )
            }
        }

        // Section 3: Countdowns
        item {
            Spacer(modifier = Modifier.height(Spacing.xs))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "روزشمار رویدادها",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "شمارش معکوس تا آزمون‌ها، پروژه‌ها و سررسیدها",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = { onIntent(WellnessIntent.ShowCountdownDialog) }) {
                    Icon(Icons.Rounded.Add, contentDescription = "روزشمار جدید", tint = EmeraldPrimary)
                }
            }
        }

        if (state.countdowns.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "هیچ روزشماری فعال نیست. برای مناسبت‌های مهم روزشمار بسازید.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(state.countdowns, key = { it.id }) { cd ->
                CountdownCard(
                    countdown = cd,
                    onDelete = { onIntent(WellnessIntent.DeleteCountdown(cd.id)) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(pageBottomInset()))
        }
    }

    // Habit Dialog
    if (state.showHabitDialog) {
        AlertDialog(
            onDismissRequest = { onIntent(WellnessIntent.DismissHabitDialog) },
            title = { Text("تعریف عادت برای ترک") },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.formBadTitle,
                        onValueChange = { onIntent(WellnessIntent.SetFormBadTitle(it)) },
                        label = { Text("عادت نامطلوب (مثلاً: گشتن بی‌هدف در اینستاگرام)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.formGoodTitle,
                        onValueChange = { onIntent(WellnessIntent.SetFormGoodTitle(it)) },
                        label = { Text("جایگزین مثبت (مثلاً: خواندن ۱۰ صفحه کتاب)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(WellnessIntent.SaveHabit) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(WellnessIntent.DismissHabitDialog) }) {
                    Text("انصراف")
                }
            }
        )
    }

    // Countdown Dialog
    if (state.showCountdownDialog) {
        AlertDialog(
            onDismissRequest = { onIntent(WellnessIntent.DismissCountdownDialog) },
            title = { Text("روزشمار رویداد جدید") },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.formCountdownTitle,
                        onValueChange = { onIntent(WellnessIntent.SetFormCountdownTitle(it)) },
                        label = { Text("عنوان رویداد") },
                        placeholder = { Text("مثلاً: کنکور ارشد یا تحویل پروژه") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.formCountdownDate,
                        onValueChange = { onIntent(WellnessIntent.SetFormCountdownDate(it)) },
                        label = { Text("تاریخ جلالی (۱۴۰۵/۰۴/۱۵)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(WellnessIntent.SaveCountdown) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(WellnessIntent.DismissCountdownDialog) }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
fun BadHabitCard(
    habit: BadHabit,
    isCleanToday: Boolean,
    onToggleClean: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = onToggleClean) {
                    Icon(
                        imageVector = if (isCleanToday) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isCleanToday) EmeraldPrimary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "🚫 ${habit.badTitle}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "✨ جایگزین: ${habit.goodTitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = "روزهای پاک: ${habit.cleanDays.size} روز",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CountdownCard(
    countdown: CountdownEvent,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(countdown.icon, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = countdown.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "تاریخ سررسید: ${countdown.dateKey}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
