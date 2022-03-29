package se.joelbit.dialife.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

interface OpenDiaryEntryDataSource {
    suspend fun set(entry: DiaryEntry)
    suspend fun close()
    fun get(): StateFlow<OpenDiaryEntry>
}