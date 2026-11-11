package ir.tadbir.app.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ViewIntent

interface ViewState

interface ViewSideEffect

/**
 * MVI architecture BaseViewModel inspired by Daneshyar Clean Architecture:
 * - Single source of truth immutable StateFlow
 * - Undispatched initial intent collection
 * - Buffered channel for one-time side effects (navigation, snackbar, toast)
 */
abstract class BaseViewModel<Intent : ViewIntent, UiState : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    abstract fun setInitialState(): UiState

    abstract fun handleIntents(intent: Intent)

    private val state: MutableStateFlow<UiState> by lazy { MutableStateFlow(setInitialState()) }
    private val readOnlyState: StateFlow<UiState> by lazy { state.asStateFlow() }
    val viewState: StateFlow<UiState>
        get() = readOnlyState

    private val intents = MutableSharedFlow<Intent>(extraBufferCapacity = 1)
    private val effects = Channel<Effect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    init {
        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            intents.collect(::handleIntents)
        }
    }

    fun setIntent(intent: Intent) {
        viewModelScope.launch { intents.emit(intent) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        state.update(reducer)
    }

    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch { effects.send(builder()) }
    }
}
