package ir.tadbir.app.ui.features.inbox

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.EnergyLevel
import ir.tadbir.app.domain.model.InboxItem
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.CyanAccent
import ir.tadbir.app.ui.theme.EmeraldPrimary

@Composable
fun InboxScreen(
    state: InboxUiState,
    onIntent: (InboxIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(InboxIntent.OpenAddModal) },
                containerColor = EmeraldPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = pageBottomInset())
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "ایده جدید")
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
            item {
                Column {
                    Text(
                        text = "صندوق ذهن (Brain Dump)",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "ذهنت را خالی کن؛ ایده‌ها، یادداشت‌ها و کارهای پراکنده را ثبت کن",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            if (state.items.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Box(
                            modifier = Modifier.padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Rounded.Inbox,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "صندوق ذهن خالی است. هر چه به ذهنت می‌رسد را با + ثبت کن!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(state.items, key = { it.id }) { item ->
                    InboxCardItem(
                        item = item,
                        onDelete = { onIntent(InboxIntent.DeleteItem(item.id)) },
                        onScheduleToday = {
                            onIntent(
                                InboxIntent.ScheduleItem(
                                    id = item.id,
                                    dateKey = JalaliDate.today().key,
                                    startTime = null
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    if (state.isAddModalOpen) {
        var text by remember { mutableStateOf("") }
        var estimateMin by remember { mutableIntStateOf(15) }

        AlertDialog(
            onDismissRequest = { onIntent(InboxIntent.CloseAddModal) },
            title = { Text("ثبت سریع در صندوق ذهن", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("چیزی که تو ذهنت هست رو بنویس...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    OutlinedTextField(
                        value = estimateMin.toString(),
                        onValueChange = { estimateMin = it.toIntOrNull() ?: 15 },
                        label = { Text("تخمین زمان (دقیقه)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            onIntent(
                                InboxIntent.CreateItem(
                                    text = text,
                                    estimateMin = estimateMin,
                                    energy = EnergyLevel.MEDIUM
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("ثبت در صندوق")
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(InboxIntent.CloseAddModal) }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
private fun InboxCardItem(
    item: InboxItem,
    onDelete: () -> Unit,
    onScheduleToday: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CyanAccent.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "⏱️ ${JalaliDate.toFa(item.estimateMin)} دقیقه",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Convert to Today's Task Button
            IconButton(onClick = onScheduleToday) {
                Icon(
                    Icons.Rounded.CalendarMonth,
                    contentDescription = "انتقال به امروز",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Delete Button
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
