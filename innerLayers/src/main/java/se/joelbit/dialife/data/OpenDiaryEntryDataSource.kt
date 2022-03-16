package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

interface OpenDiaryEntryDataSource {
    fun set(entry: DiaryEntry)
    fun close()
    fun get(): OpenDiaryEntry
}