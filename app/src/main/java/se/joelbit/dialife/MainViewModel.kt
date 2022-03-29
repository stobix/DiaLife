package se.joelbit.dialife

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.structure.DataPackage
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper

class MainViewModel(
 private val useCases: MainActivity.MainUseCases,
 val iconMapper: DisplayIconMapper,
 private val entryMapper: DisplayDiaryEntryMapper,
 private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {

 fun addEntry(entry: DiaryEntry) = modifyEntries {
  useCases.addEntry(entry)
 }

 fun updateEntry(entry: DisplayDiaryEntry) = modifyEntries {
  useCases.updateEntry(entryMapper(entry))
 }

 fun removeEntry(entry: DiaryEntry) = modifyEntries {
  useCases.removeEntry(entry.id)
 }
 fun removeEntry(entry: DisplayDiaryEntry) = modifyEntries {
  useCases.removeEntry(entry.id)
 }

    // Since this updates each time the source gets updated, we basically removes all entries, one update at a time.
    // Don't write code this way.
// fun removeLastEntry() = modifyEntries {
//  useCases.getEntries().collect { list ->
//   list.lastOrNull()?.let { entry ->
//    Log.d("Flow", "Removing entry $entry")
//    removeEntry(entry)
//   }
//  }
// }


 private fun modifyEntries( f: suspend () -> Unit) = viewModelScope.launch {
  f()
 }


 val entryFlow =
  useCases.getEntries().transformLatest { entries ->
   emit(DataPackage.Loading)
      try {
       emit(DataPackage.Data(entryMapper(entries)))
      } catch (error: Throwable) {
       emit(DataPackage.Error(error))
      }
  }.catch { error ->
   emit(DataPackage.Error(error))
  }


    val activeEntryFlow =
     useCases.getOpenEntry().map { openEntryMapper(it) }

 fun setActiveEntry(entry: DisplayDiaryEntry?) {
  viewModelScope.launch {
   entry?.let {
    useCases.setOpenEntry(entryMapper(it))
   } ?: useCases.clearOpenEntry()
  }
 }
}