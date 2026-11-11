package ir.tadbir.app.ui.features.analytics

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.AmberGold
import ir.tadbir.app.ui.theme.CyanAccent
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.RoseRed

@Composable
fun AnalyticsScreen(
    state: AnalyticsUiState,
    onIntent: (AnalyticsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = pageBottomInset()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "آمار و کارایی شخصی",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "روند رشد، استریک‌ها و ساعات تمرکز عمیق",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        // 2x2 Grid of Personal Records
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "کارهای انجام شده",
                        value = JalaliDate.toFa(state.totalTasksCompleted),
                        icon = Icons.Rounded.CheckCircle,
                        color = EmeraldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "بیشترین زنجیره",
                        value = "${JalaliDate.toFa(state.bestStreakDays)} روز",
                        icon = Icons.Rounded.LocalFireDepartment,
                        color = AmberGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "ساعات تمرکز",
                        value = "${JalaliDate.toFa(state.focusHoursTotal)} ساعت",
                        icon = Icons.Rounded.Timer,
                        color = CyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "مجموع تجربه (XP)",
                        value = "${JalaliDate.toFa(state.xpTotal)} XP",
                        icon = Icons.Rounded.EmojiEvents,
                        color = RoseRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Weekly Bar Chart
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "نرخ تکمیل کارهای هفته (شنبه تا جمعه)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val days = JalaliDate.WEEKDAY_NAMES
                        state.weeklyCompletionRate.forEachIndexed { index, rate ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 18.dp, height = (rate * 90).coerceAtLeast(10f).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (index == 6) RoseRed.copy(alpha = 0.7f) else EmeraldPrimary)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = days.getOrElse(index) { "" }.take(2),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (index == 6) RoseRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
