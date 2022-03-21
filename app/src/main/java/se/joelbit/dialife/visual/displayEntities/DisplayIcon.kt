package se.joelbit.dialife.visual.displayEntities

import androidx.annotation.DrawableRes

interface DisplayIcon {
    @get:DrawableRes
    val resId: Int
    val ordinal: Int
}