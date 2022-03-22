package se.joelbit.dialife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.joelbit.dialife.MainActivity
import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.network.ktorServer.KtorDbProvider
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper

class MainViewModel(val dataServer: KtorDbProvider
)
 : ViewModel() {
}