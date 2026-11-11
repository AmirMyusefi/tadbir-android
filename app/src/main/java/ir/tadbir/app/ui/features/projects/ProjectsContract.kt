package ir.tadbir.app.ui.features.projects

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.Project
import ir.tadbir.app.domain.model.ProjectCategory
import ir.tadbir.app.domain.model.ProjectPriority
import ir.tadbir.app.domain.model.ProjectStatus

@Immutable
data class ProjectsUiState(
    val projects: List<Project> = emptyList(),
    val selectedStatus: ProjectStatus? = null,
    val showAddSheet: Boolean = false,
    val showDetailFor: Project? = null,
    // Form
    val formTitle: String = "",
    val formIcon: String = "📁",
    val formColor: String = "#2196F3",
    val formDescription: String = "",
    val formStatus: ProjectStatus = ProjectStatus.ACTIVE,
    val formPriority: ProjectPriority = ProjectPriority.MEDIUM,
    val formCategory: ProjectCategory = ProjectCategory.PERSONAL,
    val formDeadlineKey: String = "",
    val editingProjectId: String? = null
) : ViewState

sealed interface ProjectsIntent : ViewIntent {
    data object LoadProjects : ProjectsIntent
    data class FilterByStatus(val status: ProjectStatus?) : ProjectsIntent
    data object ShowAddSheet : ProjectsIntent
    data object DismissSheet : ProjectsIntent
    data class ShowDetail(val project: Project) : ProjectsIntent
    data object DismissDetail : ProjectsIntent

    // Form fields
    data class SetFormTitle(val v: String) : ProjectsIntent
    data class SetFormIcon(val v: String) : ProjectsIntent
    data class SetFormDescription(val v: String) : ProjectsIntent
    data class SetFormStatus(val v: ProjectStatus) : ProjectsIntent
    data class SetFormPriority(val v: ProjectPriority) : ProjectsIntent
    data class SetFormCategory(val v: ProjectCategory) : ProjectsIntent
    data class SetFormDeadlineKey(val v: String) : ProjectsIntent

    data object SaveProject : ProjectsIntent
    data class DeleteProject(val id: String) : ProjectsIntent
    data class UpdateStatus(val id: String, val newStatus: ProjectStatus) : ProjectsIntent
}

sealed interface ProjectsUiEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String) : ProjectsUiEffect
}
