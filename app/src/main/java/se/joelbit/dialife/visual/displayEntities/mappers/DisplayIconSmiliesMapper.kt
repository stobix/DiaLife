package se.joelbit.dialife.visual.displayEntities.mappers

import androidx.annotation.DrawableRes
import se.joelbit.dialife.R
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIcon

// We can't inherit from enum directly, and enum can't inherit from stuff, so we must build a bunch of wrappers to make this switchable.
class DisplayIconSmiliesMapper: DisplayIconMapper {
    enum class DisplayIconEnum(@DrawableRes val resId: Int) {
        Checked(R.drawable.ic_smiley1),
        Star(R.drawable.ic_neutral1),
        Unchecked(R.drawable.ic_frown1),
        Error(R.drawable.ic_error1),
        ;
        companion object {
            fun fromIcon(icon: Icon) = values().firstOrNull { it.name == icon.name } ?: Error
            fun fromOrdinal(ord: Int) = values().firstOrNull { it.ordinal == ord } ?: Error
        }
    }

    data class DisplayIcon1(val value: DisplayIconEnum): DisplayIcon {
        override val resId = value.resId
        override val ordinal = value.ordinal
    }

    override fun fromIcon(icon: Icon) =
        DisplayIconEnum.fromIcon(icon).let {
            DisplayIcon1(it)
        }

    override fun toIcon(icon: DisplayIcon) =
        Icon.fromOrdinal(icon.ordinal)
}

