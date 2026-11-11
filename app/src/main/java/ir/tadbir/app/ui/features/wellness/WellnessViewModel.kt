package ir.tadbir.app.ui.features.wellness

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.tadbir.app.core.base.BaseViewModel
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.domain.model.BadHabit
import ir.tadbir.app.domain.model.CountdownEvent
import ir.tadbir.app.domain.model.MoodEntry
import ir.tadbir.app.domain.repository.WellnessRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WellnessViewModel @Inject constructor(
    private val repo: WellnessRepository
) : BaseViewModel<WellnessIntent, WellnessUiState, WellnessUiEffect>() {

    override fun setInitialState(): WellnessUiState = WellnessUiState()

    init {
        handleIntents(WellnessIntent.LoadData)
    }

    override fun handleIntents(intent: WellnessIntent) {
        val todayKey = JalaliDate.today().key

        when (intent) {
            is WellnessIntent.LoadData -> {
                viewModelScope.launch {
                    val mood = repo.getMoodForDate(todayKey)
                    setState { copy(todayMood = mood) }
                }
                repo.getBadHabits()
                    .onEach { list -> setState { copy(badHabits = list) } }
                    .launchIn(viewModelScope)

                repo.getCountdowns()
                    .onEach { list -> setState { copy(countdowns = list) } }
                    .launchIn(viewModelScope)
            }

            is WellnessIntent.SetTodayMood -> {
                viewModelScope.launch {
                    val entry = MoodEntry(dateKey = todayKey, mood = intent.mood, note = intent.note)
                    repo.saveMood(entry)
                    setState { copy(todayMood = entry) }
                }
            }

            is WellnessIntent.ShowHabitDialog ->
                setState { copy(showHabitDialog = true, formBadTitle = "", formGoodTitle = "") }

            is WellnessIntent.DismissHabitDialog ->
                setState { copy(showHabitDialog = false) }

            is WellnessIntent.SetFormBadTitle -> setState { copy(formBadTitle = intent.v) }
            is WellnessIntent.SetFormGoodTitle -> setState { copy(formGoodTitle = intent.v) }

            is WellnessIntent.SaveHabit -> {
                val s = viewState.value
                if (s.formBadTitle.isBlank() || s.formGoodTitle.isBlank()) {
                    setEffect { WellnessUiEffect.ShowSnackbar("عادت منفی و جایگزین مثبت آن را وارد کنید") }
                    return
                }
                viewModelScope.launch {
                    val habit = BadHabit(
                        id = UUID.randomUUID().toString(),
                        badTitle = s.formBadTitle.trim(),
                        goodTitle = s.formGoodTitle.trim()
                    )
                    repo.insertOrUpdateBadHabit(habit)
                    setState { copy(showHabitDialog = false) }
                }
            }

            is WellnessIntent.DeleteHabit -> {
                viewModelScope.launch { repo.deleteBadHabit(intent.id) }
            }

            is WellnessIntent.ToggleCleanToday -> {
                viewModelScope.launch { repo.toggleCleanDay(intent.habitId, todayKey) }
            }

            is WellnessIntent.ShowCountdownDialog ->
                setState { copy(showCountdownDialog = true, formCountdownTitle = "", formCountdownDate = "") }

            is WellnessIntent.DismissCountdownDialog ->
                setState { copy(showCountdownDialog = false) }

            is WellnessIntent.SetFormCountdownTitle ->
                setState { copy(formCountdownTitle = intent.v) }

            is WellnessIntent.SetFormCountdownDate ->
                setState { copy(formCountdownDate = intent.v) }

            is WellnessIntent.SaveCountdown -> {
                val s = viewState.value
                if (s.formCountdownTitle.isBlank() || s.formCountdownDate.isBlank()) {
                    setEffect { WellnessUiEffect.ShowSnackbar("عنوان و تاریخ رویداد را وارد کنید") }
                    return
                }
                viewModelScope.launch {
                    val cd = CountdownEvent(
                        id = UUID.randomUUID().toString(),
                        title = s.formCountdownTitle.trim(),
                        dateKey = s.formCountdownDate.trim()
                    )
                    repo.insertOrUpdateCountdown(cd)
                    setState { copy(showCountdownDialog = false) }
                }
            }

            is WellnessIntent.DeleteCountdown -> {
                viewModelScope.launch { repo.deleteCountdown(intent.id) }
            }
        }
    }
}
