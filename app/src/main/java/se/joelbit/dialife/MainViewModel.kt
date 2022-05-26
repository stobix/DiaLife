package se.joelbit.dialife

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.common.DataPackage
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper
import java.io.File

class MainViewModel(
 private val useCases: MainActivity.MainUseCases,
 val iconMapper: DisplayIconMapper,
 private val entryMapper: DisplayDiaryEntryMapper,
 private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {


 val hasCamPerm = mutableStateOf(false)

 val mediaDir = mutableStateOf<File>(File(""))


 fun addEntry(entry: DiaryEntry) = modifyEntries {
  Log.d("Entry", "Saving $entry")
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
   emit(se.joelbit.common.DataPackage.Loading)
      try {
       emit(se.joelbit.common.DataPackage.Data(entryMapper(entries)))
      } catch (error: Throwable) {
       emit(se.joelbit.common.DataPackage.Error(error))
      }
  }.catch { error ->
   emit(se.joelbit.common.DataPackage.Error(error))
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