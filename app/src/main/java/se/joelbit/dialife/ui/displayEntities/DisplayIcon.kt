package se.joelbit.dialife.ui.displayEntities

import androidx.annotation.DrawableRes

interface DisplayIcon {
    @get:DrawableRes
    val resId: Int
    val ordinal: Int
}