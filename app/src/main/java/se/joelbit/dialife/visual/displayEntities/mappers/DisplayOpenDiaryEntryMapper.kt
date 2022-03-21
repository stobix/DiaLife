package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry

interface DisplayOpenDiaryEntryMapper{
    fun toDisplay(entry: OpenDiaryEntry): DisplayOpenDiaryEntry
    fun fromDisplay(entry: DisplayOpenDiaryEntry): OpenDiaryEntry
}