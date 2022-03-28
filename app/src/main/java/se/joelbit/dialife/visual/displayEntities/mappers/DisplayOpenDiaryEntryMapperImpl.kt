package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry

class DisplayOpenDiaryEntryMapperImpl(private val entryMapper: DisplayDiaryEntryMapper) :
    DisplayOpenDiaryEntryMapper {
    override operator fun invoke(entry: OpenDiaryEntry) =
        when(entry) {
            is OpenDiaryEntry.Entry -> DisplayOpenDiaryEntry.Entry(entryMapper(entry.entry))
            OpenDiaryEntry.None -> DisplayOpenDiaryEntry.None
        }

    override operator fun invoke(entry: DisplayOpenDiaryEntry) =
        when(entry) {
            is DisplayOpenDiaryEntry.Entry -> OpenDiaryEntry.Entry(entryMapper(entry.entry)
            )
            DisplayOpenDiaryEntry.None -> OpenDiaryEntry.None
        }
}