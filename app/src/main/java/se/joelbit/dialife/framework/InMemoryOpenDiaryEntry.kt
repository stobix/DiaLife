package se.joelbit.dialife.framework

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.joelbit.dialife.data.OpenDiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry

class InMemoryOpenDiaryEntry : OpenDiaryEntryDataSource {
    private var state = MutableStateFlow<OpenDiaryEntry>(OpenDiaryEntry.None)

    override suspend fun set(entry: DiaryEntry) {
        state.value = OpenDiaryEntry.Entry(entry)
    }
    override fun get() = state.asStateFlow()
    override suspend fun close() {
        state.value = OpenDiaryEntry.None
    }
}

