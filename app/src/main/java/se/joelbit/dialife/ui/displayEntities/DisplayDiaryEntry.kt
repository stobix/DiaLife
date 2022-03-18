package se.joelbit.dialife.ui.displayEntities

import java.time.LocalDateTime

data class DisplayDiaryEntry(
    val id: Long,
    val title: String?,
    val text: String,
    val datetime: LocalDateTime,
    val icon: DisplayIcon,
)


