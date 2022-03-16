package se.joelbit.dialife.ui.entryManagement

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.DiaryEntry

class EntryManagementViewModel(val useCases: MainActivity.UseCases) : ViewModel() {

    fun addEntry(entry: DiaryEntry) = GlobalScope.launch {
        useCases.addEntry(entry)
    }

    fun removeEntry(entry: DiaryEntry) = GlobalScope.launch {
        useCases.removeEntry(entry)
    }


    fun removeLastEntry() = GlobalScope.launch {
        val entry = useCases.getEntries().lastOrNull()?.let { entry ->
            removeEntry(entry)
        }
    }

}