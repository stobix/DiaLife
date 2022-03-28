package se.joelbit.dialife.visual.ui.diaryEntries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.MainUseCases
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper
import javax.inject.Inject

@HiltViewModel
class DiaryEntriesViewModel @Inject constructor(
    private val useCases: MainUseCases,
    private val entryMapper: DisplayDiaryEntryMapper,
    private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {


    private val _text = MutableLiveData<String>()

    val text: LiveData<String> = _text
}