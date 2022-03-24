package se.joelbit.dialife

import androidx.lifecycle.ViewModel
import se.joelbit.dialife.network.ktorServer.KtorDbProvider

class MainViewModel(val dataServer: KtorDbProvider
)
 : ViewModel() {
}