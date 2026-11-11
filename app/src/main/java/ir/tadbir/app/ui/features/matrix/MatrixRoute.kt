package ir.tadbir.app.ui.features.matrix

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MatrixRoute(
    modifier: Modifier = Modifier,
    viewModel: MatrixViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    MatrixScreen(
        state = state,
        onIntent = viewModel::setIntent,
        modifier = modifier
    )
}
