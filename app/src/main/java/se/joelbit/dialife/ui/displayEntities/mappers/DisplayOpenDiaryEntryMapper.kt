package se.joelbit.dialife.ui.displayEntities.mappers

import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.ui.displayEntities.DisplayOpenDiaryEntry

interface DisplayOpenDiaryEntryMapper{
    fun toDisplay(entry: OpenDiaryEntry): DisplayOpenDiaryEntry
    fun fromDisplay(entry: DisplayOpenDiaryEntry): OpenDiaryEntry
}