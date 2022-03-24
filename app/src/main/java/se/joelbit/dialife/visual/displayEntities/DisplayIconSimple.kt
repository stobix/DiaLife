package se.joelbit.dialife.visual.displayEntities

import androidx.annotation.DrawableRes

data class DisplayIconSimple(
    override val ordinal: Int,
    @DrawableRes override val resId: Int
) : DisplayIcon