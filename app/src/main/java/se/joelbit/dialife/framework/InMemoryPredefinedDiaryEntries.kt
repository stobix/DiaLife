package se.joelbit.dialife.framework

import kotlinx.coroutines.flow.flow
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import java.time.LocalDateTime

class InMemoryPredefinedDiaryEntries : DiaryEntryDataSource {
    private val entries = mutableListOf<DiaryEntry>(
        DiaryEntry(1, "ett", "en lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Happy,
        ),
        DiaryEntry(2, "tu","två lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Sad,
        ),
        DiaryEntry(3, "tre","tre lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Neutral,
        ),
    )
    override suspend fun add(entry: DiaryEntry) {
        entries.add(entry)
    }

    override suspend fun update(entry: DiaryEntry) {
        val ix = entries.indexOfFirst { it.id == entry.id }
        entries[ix] = entry
    }
    override fun getAll() = flow {
        emit(entries)
    }

    override suspend fun remove(id: Long) {
        entries.removeIf {
            it.id == id
        }
    }
}