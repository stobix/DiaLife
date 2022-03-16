package se.joelbit.dialife.framework

import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry

class InMemoryDiaryEntries : DiaryEntryDataSource {
    private val entries = mutableListOf<DiaryEntry>()
    override suspend fun add(entry: DiaryEntry) {
        entries.add(entry)
    }

    override suspend fun getAll(): List<DiaryEntry> {
        return entries
    }

    override suspend fun remove(entry: DiaryEntry) {
        entries.remove(entry)
    }
}

