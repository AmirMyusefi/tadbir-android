package ir.tadbir.app.ui.features.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProjectsRoute(
    viewModel: ProjectsViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    ProjectsScreen(
        state = state,
        onIntent = viewModel::setIntent
    )
}
