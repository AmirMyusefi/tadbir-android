package ir.tadbir.app.ui.features.analytics

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<AnalyticsIntent, AnalyticsUiState, AnalyticsUiEffect>() {

    override fun setInitialState(): AnalyticsUiState = AnalyticsUiState()

    override fun handleIntents(intent: AnalyticsIntent) {
        // No-op for read-only analytics
    }
}
