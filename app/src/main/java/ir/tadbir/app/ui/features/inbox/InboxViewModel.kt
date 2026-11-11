package ir.tadbir.app.ui.features.inbox

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.domain.model.InboxItem
import ir.tadbir.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<InboxIntent, InboxUiState, InboxUiEffect>() {

    override fun setInitialState(): InboxUiState = InboxUiState()

    init {
        repository.getInboxItems()
            .onEach { items ->
                setState { copy(items = items) }
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntents(intent: InboxIntent) {
        when (intent) {
            InboxIntent.OpenAddModal -> setState { copy(isAddModalOpen = true) }
            InboxIntent.CloseAddModal -> setState { copy(isAddModalOpen = false) }
            is InboxIntent.CreateItem -> {
                viewModelScope.launch {
                    val item = InboxItem(
                        id = UUID.randomUUID().toString(),
                        text = intent.text,
                        estimateMin = intent.estimateMin,
                        energy = intent.energy
                    )
                    repository.insertInboxItem(item)
                    setState { copy(isAddModalOpen = false) }
                    setEffect { InboxUiEffect.ShowToast("ایده به صندوق ذهن افزوده شد 💡") }
                }
            }
            is InboxIntent.DeleteItem -> {
                viewModelScope.launch {
                    repository.deleteInboxItem(intent.id)
                    setEffect { InboxUiEffect.ShowToast("آیتم حذف شد") }
                }
            }
            is InboxIntent.ScheduleItem -> {
                viewModelScope.launch {
                    repository.convertInboxItemToTask(intent.id, intent.dateKey, intent.startTime)
                    setEffect { InboxUiEffect.ShowToast("آیتم به کار روزانه تبدیل شد 🚀") }
                }
            }
        }
    }
}
