package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIcon

interface DisplayIconMapper {
    fun fromIcon(icon: Icon): DisplayIcon
    fun toIcon(icon: DisplayIcon): Icon
}