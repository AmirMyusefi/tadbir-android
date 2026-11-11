package ir.tadbir.app.data.local

import ir.tadbir.app.domain.model.Project
import ir.tadbir.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {

    override fun getProjects(): Flow<List<Project>> =
        projectDao.getProjects().map { it.map { e -> e.toDomain() } }

    override suspend fun getProjectById(id: String): Project? =
        projectDao.getProjectById(id)?.toDomain()

    override suspend fun insertOrUpdateProject(project: Project) =
        projectDao.insertOrUpdate(ProjectEntity.fromDomain(project))

    override suspend fun deleteProject(id: String) {
        projectDao.deleteProject(id)
    }
}
