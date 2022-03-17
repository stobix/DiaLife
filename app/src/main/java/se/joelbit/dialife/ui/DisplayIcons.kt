package se.joelbit.dialife.ui

import androidx.annotation.DrawableRes
import se.joelbit.dialife.domain.Icon

enum class DisplayIcon(@DrawableRes val resId: Int) {
    Checked(android.R.drawable.checkbox_on_background),
    Star(android.R.drawable.star_on),
    Unchecked(android.R.drawable.checkbox_off_background),
    Error(android.R.drawable.stat_notify_error),
    ;
    companion object {
        fun fromIcon(icon: Icon) = values().firstOrNull { it.name == icon.name } ?: Error
        fun fromOrdinal(ord: Int) = values().firstOrNull { it.ordinal == ord } ?: Error
    }
}

val Icon.res get() =
    DisplayIcon.fromIcon(this).resId