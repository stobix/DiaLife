package se.joelbit.dialife.ui.diaryEntries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class DiaryEntriesViewModel(val useCases: MainActivity.UseCases) : ViewModel() {

    private val _text = MutableLiveData<String>()

    private val _entries = MutableLiveData<List<DiaryEntry>>()

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            val fetched = useCases.getEntries()
            _entries.postValue(fetched)
            if(fetched.isEmpty())
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

    fun setActiveEntry(entry: DiaryEntry?) {
        viewModelScope.launch {
            entry?.let {
                useCases.setOpenEntry(it)
            } ?: useCases.clearOpenEntry()
        }
    }

    fun displayActiveEntry() {
        viewModelScope.launch {
            when(val openEntry = useCases.getOpenEntry()) {
                is OpenDiaryEntry.Entry ->
                    _text.postValue("Last clicked entry: ${openEntry.entry.text}")
                OpenDiaryEntry.None ->
                    _text.postValue("")
            }
        }
    }

    val text: LiveData<String> = _text
    val entries: LiveData<List<DiaryEntry>> = _entries
}