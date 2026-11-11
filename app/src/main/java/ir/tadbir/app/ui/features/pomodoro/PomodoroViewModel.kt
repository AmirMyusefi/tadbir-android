package ir.tadbir.app.ui.features.pomodoro

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor() :
    BaseViewModel<PomodoroIntent, PomodoroUiState, PomodoroUiEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroUiState = PomodoroUiState()

    override fun handleIntents(intent: PomodoroIntent) {
        when (intent) {
            PomodoroIntent.ToggleTimer -> {
                if (viewState.value.isRunning) {
                    pauseTimer()
                } else {
                    startTimer()
                }
            }
            PomodoroIntent.ResetTimer -> {
                pauseTimer()
                setState {
                    copy(
                        remainingSeconds = mode.defaultMinutes * 60,
                        isRunning = false
                    )
                }
            }
            is PomodoroIntent.ChangeMode -> {
                pauseTimer()
                setState {
                    copy(
                        mode = intent.mode,
                        remainingSeconds = intent.mode.defaultMinutes * 60,
                        isRunning = false
                    )
                }
            }
            PomodoroIntent.Tick -> {
                val current = viewState.value.remainingSeconds
                if (current > 1) {
                    setState { copy(remainingSeconds = current - 1) }
                } else {
                    pauseTimer()
                    val newCompleted = if (viewState.value.mode == PomodoroMode.FOCUS) {
                        viewState.value.completedSessionsCount + 1
                    } else {
                        viewState.value.completedSessionsCount
                    }
                    setState {
                        copy(
                            remainingSeconds = 0,
                            isRunning = false,
                            completedSessionsCount = newCompleted
                        )
                    }
                    setEffect { PomodoroUiEffect.Notify("جلسه تمرکز با موفقیت تمام شد! آفرین 🎉") }
                }
            }
        }
    }

    private fun startTimer() {
        setState { copy(isRunning = true) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && viewState.value.isRunning) {
                delay(1000L)
                handleIntents(PomodoroIntent.Tick)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        setState { copy(isRunning = false) }
    }
}
