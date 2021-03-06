package se.joelbit.dialife.framework

import se.joelbit.dialife.data.OpenDiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class InMemoryOpenDiaryEntry : OpenDiaryEntryDataSource {
    private var state : OpenDiaryEntry = OpenDiaryEntry.None
    override suspend fun set(entry: DiaryEntry) {
        state = OpenDiaryEntry.Entry(entry)
    }
    override suspend fun get() = state
    override suspend fun close() {
        state = OpenDiaryEntry.None
    }
}

