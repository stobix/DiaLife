package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry

class DisplayOpenDiaryEntryMapperImpl(private val entryMapper: DisplayDiaryEntryMapper) :
    DisplayOpenDiaryEntryMapper {
    override fun toDisplay(entry: OpenDiaryEntry) =
        when(entry) {
            is OpenDiaryEntry.Entry -> DisplayOpenDiaryEntry.Entry(entryMapper.toDisplayEntry(entry.entry))
            OpenDiaryEntry.None -> DisplayOpenDiaryEntry.None
        }

    override fun fromDisplay(entry: DisplayOpenDiaryEntry) =
        when(entry) {
            is DisplayOpenDiaryEntry.Entry -> OpenDiaryEntry.Entry(
                entryMapper.fromDisplayEntry(
                    entry.entry
                )
            )
            DisplayOpenDiaryEntry.None -> OpenDiaryEntry.None
        }

}