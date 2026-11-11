package ir.tadbir.app.ui.features.projects

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.model.Project
import ir.tadbir.app.domain.model.ProjectCategory
import ir.tadbir.app.domain.model.ProjectPriority
import ir.tadbir.app.domain.model.ProjectStatus
import ir.tadbir.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepo: ProjectRepository
) : BaseViewModel<ProjectsIntent, ProjectsUiState, ProjectsUiEffect>() {

    override fun setInitialState(): ProjectsUiState = ProjectsUiState()

    init {
        handleIntents(ProjectsIntent.LoadProjects)
    }

    override fun handleIntents(intent: ProjectsIntent) {
        when (intent) {
            is ProjectsIntent.LoadProjects -> {
                projectRepo.getProjects()
                    .onEach { list -> setState { copy(projects = list) } }
                    .launchIn(viewModelScope)
            }

            is ProjectsIntent.FilterByStatus -> {
                setState { copy(selectedStatus = intent.status) }
            }

            is ProjectsIntent.ShowAddSheet -> {
                setState {
                    copy(
                        showAddSheet = true,
                        editingProjectId = null,
                        formTitle = "",
                        formIcon = "📁",
                        formDescription = "",
                        formStatus = ProjectStatus.ACTIVE,
                        formPriority = ProjectPriority.MEDIUM,
                        formCategory = ProjectCategory.PERSONAL,
                        formDeadlineKey = ""
                    )
                }
            }

            is ProjectsIntent.DismissSheet -> setState { copy(showAddSheet = false) }
            is ProjectsIntent.ShowDetail -> setState { copy(showDetailFor = intent.project) }
            is ProjectsIntent.DismissDetail -> setState { copy(showDetailFor = null) }

            is ProjectsIntent.SetFormTitle -> setState { copy(formTitle = intent.v) }
            is ProjectsIntent.SetFormIcon -> setState { copy(formIcon = intent.v) }
            is ProjectsIntent.SetFormDescription -> setState { copy(formDescription = intent.v) }
            is ProjectsIntent.SetFormStatus -> setState { copy(formStatus = intent.v) }
            is ProjectsIntent.SetFormPriority -> setState { copy(formPriority = intent.v) }
            is ProjectsIntent.SetFormCategory -> setState { copy(formCategory = intent.v) }
            is ProjectsIntent.SetFormDeadlineKey -> setState { copy(formDeadlineKey = intent.v) }

            is ProjectsIntent.SaveProject -> {
                val s = viewState.value
                if (s.formTitle.isBlank()) {
                    setEffect { ProjectsUiEffect.ShowSnackbar("عنوان پروژه را وارد کنید") }
                    return
                }
                viewModelScope.launch {
                    val existing = s.editingProjectId?.let { projectRepo.getProjectById(it) }
                    val project = (existing ?: Project(
                        id = UUID.randomUUID().toString(),
                        title = s.formTitle
                    )).copy(
                        title = s.formTitle,
                        icon = s.formIcon,
                        description = s.formDescription,
                        status = s.formStatus,
                        priority = s.formPriority,
                        category = s.formCategory,
                        deadlineKey = s.formDeadlineKey
                    )
                    projectRepo.insertOrUpdateProject(project)
                    setState { copy(showAddSheet = false) }
                }
            }

            is ProjectsIntent.DeleteProject -> {
                viewModelScope.launch {
                    projectRepo.deleteProject(intent.id)
                }
            }

            is ProjectsIntent.UpdateStatus -> {
                viewModelScope.launch {
                    val p = projectRepo.getProjectById(intent.id) ?: return@launch
                    projectRepo.insertOrUpdateProject(p.copy(status = intent.newStatus))
                }
            }
        }
    }
}
