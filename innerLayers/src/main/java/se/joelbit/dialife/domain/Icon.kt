package se.joelbit.dialife.domain

enum class Icon() {
        Checked, Star, Unchecked, Error
        ;
        companion object {
                fun fromOrdinal(ord: Int) = values().firstOrNull { it.ordinal == ord } ?: Error
        }
}