package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.R
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIconSimple

class DisplayIconSmiliesMapper: DisplayIconMapperSimple (
    { icon ->
        DisplayIconSimple(
            ordinal = icon.ordinal,
            resId = when(icon) {
                Icon.Happy -> R.drawable.ic_smiley1
                Icon.Neutral -> R.drawable.ic_neutral1
                Icon.Sad -> R.drawable.ic_frown1
                else -> R.drawable.ic_error1
            }
        )
    }
)
