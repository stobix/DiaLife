package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry

import kotlinx.coroutines.flow.Flow

interface DiaryEntryDataSource {
    suspend fun add(entry: DiaryEntry)
    suspend fun update(entry: DiaryEntry)
    fun getAll(): Flow<List<DiaryEntry>>
    suspend fun remove(id: Long)
}


