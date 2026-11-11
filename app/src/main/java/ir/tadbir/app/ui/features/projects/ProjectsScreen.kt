package ir.tadbir.app.ui.features.projects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.domain.model.Project
import ir.tadbir.app.domain.model.ProjectCategory
import ir.tadbir.app.domain.model.ProjectPriority
import ir.tadbir.app.domain.model.ProjectStatus
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    state: ProjectsUiState,
    onIntent: (ProjectsIntent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredProjects = if (state.selectedStatus == null) {
        state.projects
    } else {
        state.projects.filter { it.status == state.selectedStatus }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            item {
                Spacer(modifier = Modifier.height(Spacing.xs))
                // Banner
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF2196F3).copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📁", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "مدیریت پروژه‌ها (کانبان)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "دسته‌بندی و پیشبرد وظایف در قالب پروژه‌های مجزا",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Status Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedStatus == null,
                            onClick = { onIntent(ProjectsIntent.FilterByStatus(null)) },
                            label = { Text("همه (${state.projects.size})") }
                        )
                    }
                    items(ProjectStatus.entries) { st ->
                        val count = state.projects.count { it.status == st }
                        FilterChip(
                            selected = state.selectedStatus == st,
                            onClick = { onIntent(ProjectsIntent.FilterByStatus(st)) },
                            label = { Text("${st.emoji} ${st.titleFa} ($count)") }
                        )
                    }
                }
            }

            if (filteredProjects.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📂", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "پروژه‌ای در این بخش وجود ندارد",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "برای شروع، پروژه جدید ایجاد کنید",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                items(filteredProjects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        onClick = { onIntent(ProjectsIntent.ShowDetail(project)) },
                        onDelete = { onIntent(ProjectsIntent.DeleteProject(project.id)) },
                        onStatusChange = { newSt ->
                            onIntent(ProjectsIntent.UpdateStatus(project.id, newSt))
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(pageBottomInset()))
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { onIntent(ProjectsIntent.ShowAddSheet) },
            containerColor = Color(0xFF2196F3),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = pageBottomInset() + 16.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "پروژه جدید")
        }

        // Add Sheet
        if (state.showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = { onIntent(ProjectsIntent.DismissSheet) },
                sheetState = bottomSheetState
            ) {
                AddProjectSheetContent(state = state, onIntent = onIntent)
            }
        }

        // Detail Sheet
        state.showDetailFor?.let { project ->
            ModalBottomSheet(
                onDismissRequest = { onIntent(ProjectsIntent.DismissDetail) },
                sheetState = bottomSheetState
            ) {
                ProjectDetailSheetContent(
                    project = project,
                    onStatusChange = { newSt ->
                        onIntent(ProjectsIntent.UpdateStatus(project.id, newSt))
                    },
                    onDelete = {
                        onIntent(ProjectsIntent.DeleteProject(project.id))
                        onIntent(ProjectsIntent.DismissDetail)
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onStatusChange: (ProjectStatus) -> Unit
) {
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
                            .background(Color(0xFF2196F3).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(project.icon, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${project.status.emoji} ${project.status.titleFa}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = " • ${project.category.titleFa}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
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

            if (project.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }

            if (project.deadlineKey.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "مهلت تحویل: ${project.deadlineKey}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Status Switcher
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProjectStatus.entries.forEach { st ->
                    val isCurrent = st == project.status
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCurrent) Color(0xFF2196F3) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onStatusChange(st) }
                    ) {
                        Text(
                            text = st.titleFa,
                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(vertical = 4.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddProjectSheetContent(
    state: ProjectsUiState,
    onIntent: (ProjectsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "پروژه جدید",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.formTitle,
            onValueChange = { onIntent(ProjectsIntent.SetFormTitle(it)) },
            label = { Text("عنوان پروژه") },
            placeholder = { Text("مثلاً: بازطراحی معماری تدبیر اندروید") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.formDescription,
            onValueChange = { onIntent(ProjectsIntent.SetFormDescription(it)) },
            label = { Text("توضیحات و اهداف") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.formDeadlineKey,
            onValueChange = { onIntent(ProjectsIntent.SetFormDeadlineKey(it)) },
            label = { Text("ددلاین (اختیاری، مثلا ۱۴۰۵/۰۲/۱۵)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("وضعیت اولیه:", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectStatus.entries.forEach { st ->
                val selected = st == state.formStatus
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) Color(0xFF2196F3) else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onIntent(ProjectsIntent.SetFormStatus(st)) }
                ) {
                    Text(
                        text = "${st.emoji} ${st.titleFa}",
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onIntent(ProjectsIntent.SaveProject) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ایجاد پروژه", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProjectDetailSheetContent(
    project: Project,
    onStatusChange: (ProjectStatus) -> Unit,
    onDelete: () -> Unit
) {
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
                Text(project.icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${project.status.emoji} ${project.status.titleFa} • دسته: ${project.category.titleFa}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.DeleteOutline, contentDescription = "حذف پروژه", tint = MaterialTheme.colorScheme.error)
            }
        }

        if (project.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        if (project.deadlineKey.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "تاریخ سررسید (ددلاین): ${project.deadlineKey}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
