package se.joelbit.dialife.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to my Android Kotlin demo app!\n\n" +
                "It showcases a simple Clean Architecture structure with MVVM, dependency injection, and a Room database.\n " +
                "Add a diary entry on the manage tab, use the Diary entries tab to view added entries."
    }
    val text: LiveData<String> = _text
}