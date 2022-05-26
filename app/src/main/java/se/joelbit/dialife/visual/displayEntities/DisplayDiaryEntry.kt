package se.joelbit.dialife.visual.displayEntities

import android.net.Uri
import se.joelbit.dialife.visual.uiComponents.ListAdapterKeyItem
import java.time.LocalDateTime

data class DisplayDiaryEntry(
    val id: Long,
    val title: String?,
    val text: String,
    val datetime: LocalDateTime,
    val icon: DisplayIcon,
    val pictures: List<DisplayPicture>,
)  : ListAdapterKeyItem<Long> {
    override fun getKey() = id
}

data class DisplayPicture(
    val id: Long,
    val uri: Uri,
    val time: LocalDateTime,
)
