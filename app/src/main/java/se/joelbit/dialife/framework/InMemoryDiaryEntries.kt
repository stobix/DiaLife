package se.joelbit.dialife.framework

import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry

class InMemoryDiaryEntries : DiaryEntryDataSource {
    private val entries = mutableListOf<DiaryEntry>()
    override suspend fun add(entry: DiaryEntry) {
        entries.add(entry)
    }

    override suspend fun update(entry: DiaryEntry) {
        val ix = entries.indexOfFirst { it.id == entry.id }
        entries[ix] = entry
    }

    override suspend fun getAll(): List<DiaryEntry> {
        return entries
    }

    override suspend fun remove(id: Long) {
        entries.removeIf { it.id == id }
    }
}

