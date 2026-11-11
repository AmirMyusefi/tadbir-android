package ir.tadbir.app.ui.features.goals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GoalsRoute(
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    GoalsScreen(
        state = state,
        onIntent = viewModel::setIntent
    )
}
