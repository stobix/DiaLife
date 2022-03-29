package se.joelbit.dialife.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class OpenDiaryEntryRepository(private val dataSource: OpenDiaryEntryDataSource) {
    suspend fun set(entry: DiaryEntry) = dataSource.set(entry)
    fun get(): StateFlow<OpenDiaryEntry> = dataSource.get()
    suspend fun close() = dataSource.close()
}