package ir.tadbir.app.domain.repository

import ir.tadbir.app.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getProjects(): Flow<List<Project>>
    suspend fun getProjectById(id: String): Project?
    suspend fun insertOrUpdateProject(project: Project)
    suspend fun deleteProject(id: String)
}
