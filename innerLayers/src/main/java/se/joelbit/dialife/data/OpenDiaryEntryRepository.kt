package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class OpenDiaryEntryRepository(private val dataSource: OpenDiaryEntryDataSource) {
    suspend fun set(entry: DiaryEntry) = dataSource.set(entry)
    suspend fun get(): OpenDiaryEntry = dataSource.get()
    suspend fun close() = dataSource.close()
}