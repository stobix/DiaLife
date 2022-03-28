package se.joelbit.dialife.visual.ui.diaryEntries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper

class DiaryEntriesViewModel(
    private val useCases: MainActivity.MainUseCases,
    private val entryMapper: DisplayDiaryEntryMapper,
    private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {


    private val _text = MutableLiveData<String>()

    val text: LiveData<String> = _text
}