package se.joelbit.dialife.domain

enum class Icon(val isOrdinary: Boolean = true) {
        Happy, Neutral, Sad, Error(false)
        ;
        companion object {
                fun fromOrdinal(ord: Int) = values().firstOrNull { it.ordinal == ord } ?: Error
        }
}