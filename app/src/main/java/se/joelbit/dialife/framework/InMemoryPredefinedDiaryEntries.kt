package se.joelbit.dialife.framework

import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry

class InMemoryPredefinedDiaryEntries : DiaryEntryDataSource {
    private val entries = mutableListOf<DiaryEntry>(
        DiaryEntry(1, "ett"),
        DiaryEntry(2, "tu"),
        DiaryEntry(3, "tre"),


    )
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