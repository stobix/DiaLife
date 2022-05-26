package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry

class DisplayOpenDiaryEntryMapperImpl(private val entryMapper: DisplayDiaryEntryMapper) :
    DisplayOpenDiaryEntryMapper {
    override operator fun invoke(entry: OpenDiaryEntry) = DisplayOpenDiaryEntry(entry.map { entryMapper(it) }.asNullable())

    override operator fun invoke(entry: DisplayOpenDiaryEntry) = OpenDiaryEntry(entry.map { entryMapper(it) }.asNullable())
}