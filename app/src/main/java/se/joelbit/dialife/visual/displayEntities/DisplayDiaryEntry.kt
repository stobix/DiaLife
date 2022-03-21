package se.joelbit.dialife.visual.displayEntities

import se.joelbit.dialife.visual.uiComponents.ListAdapterKeyItem
import java.time.LocalDateTime

data class DisplayDiaryEntry(
    val id: Long,
    val title: String?,
    val text: String,
    val datetime: LocalDateTime,
    val icon: DisplayIcon,
)  : ListAdapterKeyItem<Long> {
    override fun getKey() = id
}

