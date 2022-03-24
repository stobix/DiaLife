package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.R
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIconSimple

// We can't inherit from enum directly, and enum can't inherit from stuff, so we must build a bunch of wrappers to make this switchable.
class DisplayIconHandsMapper: DisplayIconMapperSimple(
    { icon ->
        DisplayIconSimple(
            ordinal = icon.ordinal,
            resId = when(icon) {
                Icon.Happy -> R.drawable.ic_thumbsup
                Icon.Neutral -> R.drawable.ic_thumbsneutral
                Icon.Sad -> R.drawable.ic_thumbsdown
                else -> R.drawable.ic_thumbserror
            }
        )
    }
)