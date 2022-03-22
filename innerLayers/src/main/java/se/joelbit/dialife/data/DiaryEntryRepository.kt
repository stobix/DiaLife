package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry

class DiaryEntryRepository(private val dataSource: DiaryEntryDataSource) {
    suspend fun add(entry: DiaryEntry) = dataSource.add(entry)
    suspend fun getAll():List<DiaryEntry> = dataSource.getAll()
    suspend fun update(entry: DiaryEntry) = dataSource.update(entry)
    suspend fun remove(id: Long) = dataSource.remove(id)
}

