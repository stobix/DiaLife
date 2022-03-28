package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIcon

open class DisplayIconMapperSimple(
    val fromIconMapper:(Icon) -> DisplayIcon,
    val toIconMapper: (DisplayIcon) -> Icon = { Icon.fromOrdinal(it.ordinal) }
) : DisplayIconMapper {
    override fun invoke(icon: Icon) = fromIconMapper(icon)
    override fun invoke(icon: DisplayIcon) = toIconMapper(icon)
}