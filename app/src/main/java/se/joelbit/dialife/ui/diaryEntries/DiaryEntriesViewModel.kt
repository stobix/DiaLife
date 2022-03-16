package se.joelbit.dialife.ui.diaryEntries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import se.joelbit.dialife.MainActivity

class DiaryEntriesViewModel(val useCases: MainActivity.UseCases) : ViewModel() {

    private val _text = MutableLiveData<String>()


    init {
        loadEntries()
    }

    fun loadEntries() {
        GlobalScope.launch {
            _text.postValue(
                useCases.getEntries().let {
                    if(it.isEmpty())
                        "Go to the Manage tab to add/remove entries."
                    else
                        it.foldRight("Entries so far:\n") { diaryEntry, acc ->
                            "$acc ${diaryEntry.text}\n"
                        }

                }
            )
        }
    }

    val text: LiveData<String> = _text
}