package se.joelbit.dialife.domain

import java.time.LocalDateTime

data class DiaryEntry (
        val id: Long,
        val title: String?,
        val text: String,
        val datetime: LocalDateTime,
        val icon: Icon,
        val pictures: List<Picture>,
        ) {
}


