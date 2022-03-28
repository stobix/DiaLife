package se.joelbit.dialife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.structure.DataPackage
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel
 @Inject constructor(
  private val useCases: MainUseCases,
  val iconMapper: DisplayIconMapper,
  private val entryMapper: DisplayDiaryEntryMapper,
  private val openEntryMapper: DisplayOpenDiaryEntryMapper,
) : ViewModel() {

 fun addEntry(entry: DiaryEntry) = modifyEntries {
  useCases.addEntry(entry)
 }

 fun removeEntry(entry: DiaryEntry) = modifyEntries {
  useCases.removeEntry(entry.id)
 }

 fun removeLastEntry() = modifyEntries {
  useCases.getEntries().lastOrNull()?.let { entry ->
   removeEntry(entry)
  }
 }


 private fun modifyEntries( f: suspend () -> Unit) = viewModelScope.launch {
  f()
  _entriesChangedAt.value = LocalDateTime.now()
 }


 private val _activeEntry = MutableLiveData<DisplayOpenDiaryEntry>()
 private val _entries = MutableLiveData<List<DisplayDiaryEntry>>()

 init {
//  loadEntries()
 }

 fun loadEntries() {
  viewModelScope.launch {
   val fetched = useCases.getEntries()
   val mapped = entryMapper(fetched)
   _entries.postValue(mapped)
//   if(mapped.isEmpty())
//    _text.postValue("Go to the Manage tab to add/remove entries.")
  }
 }


 private val _entriesChangedAt = MutableStateFlow(LocalDateTime.now())
 val entriesChangedAt = _entriesChangedAt as StateFlow<LocalDateTime>

 val entryFlow =
  entriesChangedAt.transformLatest {
      emit(DataPackage.Loading)
   try {
    val entries = useCases.getEntries()
    emit(DataPackage.Data(entryMapper(entries)))
   } catch (error: Throwable) {
       emit(DataPackage.Error(error))
   }
  }


 fun setActiveEntry(entry: DisplayDiaryEntry?) {
  viewModelScope.launch {
   entry?.let {
    useCases.setOpenEntry(entryMapper(it))
   } ?: useCases.clearOpenEntry()
  }
 }

 fun getActiveEntry() {
  viewModelScope.launch {
   val openEntry = useCases.getOpenEntry()
   val mappedEntry = openEntryMapper(openEntry)
   _activeEntry.postValue(mappedEntry)
//   when(openEntry) {
//    is OpenDiaryEntry.Entry ->
//     _text.postValue("Last clicked entry: ${openEntry.entry.text}")
//    OpenDiaryEntry.None ->
//     _text.postValue("")
//   }
  }
 }

 val activeEntry: LiveData<DisplayOpenDiaryEntry> = _activeEntry
 val entries: LiveData<List<DisplayDiaryEntry>> = _entries

}