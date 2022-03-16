package se.joelbit.dialife.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to my little demo app written in Kotlin!\n\n" +
                "It showcases Clean Architecture, MVVM, depencency injection via Koin with the possibility to switch between an in-memory storage and a Room database, and was built in one day."
    }
    val text: LiveData<String> = _text
}