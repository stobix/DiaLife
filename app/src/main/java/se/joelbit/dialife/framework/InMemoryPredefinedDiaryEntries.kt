package se.joelbit.dialife.framework

import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import java.time.LocalDateTime

class InMemoryPredefinedDiaryEntries : DiaryEntryDataSource {
    private val entries = mutableListOf<DiaryEntry>(
        DiaryEntry(1, "ett", "en lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Checked,
        ),
        DiaryEntry(2, "tu","två lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Unchecked,
        ),
        DiaryEntry(3, "tre","tre lång text",
            datetime = LocalDateTime.of(1,2,3,4,5,6),
            icon = Icon.Star,
        ),
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