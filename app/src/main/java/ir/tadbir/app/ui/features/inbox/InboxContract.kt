package ir.tadbir.app.ui.features.inbox

import androidx.compose.runtime.Immutable
import ir.tadbir.app.core.base.ViewIntent
import ir.tadbir.app.core.base.ViewSideEffect
import ir.tadbir.app.core.base.ViewState
import ir.tadbir.app.domain.model.EnergyLevel
import ir.tadbir.app.domain.model.InboxItem

@Immutable
data class InboxUiState(
    val items: List<InboxItem> = emptyList(),
    val isAddModalOpen: Boolean = false,
    val selectedItemToSchedule: InboxItem? = null
) : ViewState

sealed interface InboxIntent : ViewIntent {
    data object OpenAddModal : InboxIntent
    data object CloseAddModal : InboxIntent
    data class CreateItem(val text: String, val estimateMin: Int, val energy: EnergyLevel) : InboxIntent
    data class DeleteItem(val id: String) : InboxIntent
    data class ScheduleItem(val id: String, val dateKey: String, val startTime: String?) : InboxIntent
}

sealed interface InboxUiEffect : ViewSideEffect {
    data class ShowToast(val message: String) : InboxUiEffect
}
