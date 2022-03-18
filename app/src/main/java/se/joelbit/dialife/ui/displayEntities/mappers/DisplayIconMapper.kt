package se.joelbit.dialife.ui.displayEntities.mappers

import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.ui.displayEntities.DisplayIcon

interface DisplayIconMapper {
    fun fromIcon(icon: Icon): DisplayIcon
    fun toIcon(icon: DisplayIcon): Icon
}