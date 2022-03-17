package se.joelbit.dialife.ui.entryManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.DiaryEntry

class EntryManagementViewModel(private val useCases: MainActivity.UseCases) : ViewModel() {

    fun addEntry(entry: DiaryEntry) = viewModelScope.launch {
        useCases.addEntry(entry)
    }

    fun removeEntry(entry: DiaryEntry) = viewModelScope.launch {
        useCases.removeEntry(entry)
    }

    fun removeLastEntry() = viewModelScope.launch {
        val entry = useCases.getEntries().lastOrNull()?.let { entry ->
            removeEntry(entry)
        }
    }
}