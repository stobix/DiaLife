package se.joelbit.dialife.visual.ui.diaryEntries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper

class DiaryEntriesViewModel(private val useCases: MainActivity.UseCases,
                            private val entryMapper: DisplayDiaryEntryMapper,
                            private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {


    private val _text = MutableLiveData<String>()

    private val _activeEntry = MutableLiveData<DisplayOpenDiaryEntry>()
    private val _entries = MutableLiveData<List<DisplayDiaryEntry>>()

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            val fetched = useCases.getEntries()
            val mapped = fetched.map { entryMapper.toDisplayEntry(it)  }
            _entries.postValue(mapped)
            if(mapped.isEmpty())
                _text.postValue("Go to the Manage tab to add/remove entries.")

//            _text.postValue(
//                if(fetched.isEmpty())
//                    "Go to the Manage tab to add/remove entries."
//                else
//                    fetched.foldRight("Entries so far:\n") { diaryEntry, acc ->
//                        "$acc ${diaryEntry.text}\n"
//                }
//            )
        }
    }

    fun setActiveEntry(entry: DisplayDiaryEntry?) {
        viewModelScope.launch {
            entry?.let {
                useCases.setOpenEntry(entryMapper.fromDisplayEntry(it))
            } ?: useCases.clearOpenEntry()
        }
    }

    fun getActiveEntry() {
        viewModelScope.launch {
            val openEntry = useCases.getOpenEntry()
            val mappedEntry = openEntryMapper.toDisplay(openEntry)
            _activeEntry.postValue(mappedEntry)
            when(openEntry) {
                is OpenDiaryEntry.Entry ->
                    _text.postValue("Last clicked entry: ${openEntry.entry.text}")
                OpenDiaryEntry.None ->
                    _text.postValue("")
            }
        }
    }

    val text: LiveData<String> = _text
    val activeEntry: LiveData<DisplayOpenDiaryEntry> = _activeEntry
    val entries: LiveData<List<DisplayDiaryEntry>> = _entries
}