package se.joelbit.dialife.domain

import java.time.LocalDateTime

data class Picture (
    val id: Long,
    val uri: String,
    val timestamp: LocalDateTime,
)