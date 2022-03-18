package se.joelbit.dialife.ui.displayEntities.mappers

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.ui.displayEntities.DisplayDiaryEntry

interface DisplayDiaryEntryMapper{
    fun toDisplayEntry(entry: DiaryEntry): DisplayDiaryEntry
    fun fromDisplayEntry(entry: DisplayDiaryEntry): DiaryEntry
}

