package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry

interface DisplayDiaryEntryMapper{
    fun toDisplayEntry(entry: DiaryEntry): DisplayDiaryEntry
    fun fromDisplayEntry(entry: DisplayDiaryEntry): DiaryEntry
}

