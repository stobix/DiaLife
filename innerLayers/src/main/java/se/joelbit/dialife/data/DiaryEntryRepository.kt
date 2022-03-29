package se.joelbit.dialife.data

import kotlinx.coroutines.flow.Flow
import se.joelbit.dialife.domain.DiaryEntry

class DiaryEntryRepository(private val dataSource: DiaryEntryDataSource) {
    suspend fun add(entry: DiaryEntry) = dataSource.add(entry)
    fun getAll(): Flow<List<DiaryEntry>> = dataSource.getAll()
    suspend fun update(entry: DiaryEntry) = dataSource.update(entry)
    suspend fun remove(id: Long) = dataSource.remove(id)
}

