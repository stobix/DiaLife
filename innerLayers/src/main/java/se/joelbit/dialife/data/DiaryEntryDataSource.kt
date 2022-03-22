package se.joelbit.dialife.data

import se.joelbit.dialife.domain.DiaryEntry

interface DiaryEntryDataSource {
    suspend fun add(entry: DiaryEntry)
    suspend fun update(entry: DiaryEntry)
    suspend fun getAll():List<DiaryEntry>
    suspend fun remove(id: Long)
}


