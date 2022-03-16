package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class DiaryEntryRepository(private val dataSource: DiaryEntryDataSource) {
    suspend fun add(entry: DiaryEntry) = dataSource.add(entry)
    suspend fun getAll():List<DiaryEntry> = dataSource.getAll()
    suspend fun remove(entry: DiaryEntry) = dataSource.remove(entry)
}

class OpenDiaryEntryRepository(private val dataSource: OpenDiaryEntryDataSource) {
    suspend fun set(entry: DiaryEntry) = dataSource.set(entry)
    suspend fun get(): OpenDiaryEntry = dataSource.get()
    suspend fun close() = dataSource.close()
}
