package ir.tadbir.app.ui.features.wellness

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WellnessRoute(
    viewModel: WellnessViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    WellnessScreen(
        state = state,
        onIntent = viewModel::setIntent
    )
}
