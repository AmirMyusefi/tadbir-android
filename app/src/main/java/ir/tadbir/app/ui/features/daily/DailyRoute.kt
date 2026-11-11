package ir.tadbir.app.ui.features.daily

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DailyRoute(
    modifier: Modifier = Modifier,
    viewModel: DailyViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DailyUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is DailyUiEffect.NavigateToTaskDetails -> {
                    // Navigate to details if needed
                }
            }
        }
    }

    DailyScreen(
        state = state,
        onIntent = viewModel::setIntent,
        modifier = modifier
    )
}
