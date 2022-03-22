package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

interface OpenDiaryEntryDataSource {
    suspend fun set(entry: DiaryEntry)
    suspend fun close()
    suspend fun get(): OpenDiaryEntry
}