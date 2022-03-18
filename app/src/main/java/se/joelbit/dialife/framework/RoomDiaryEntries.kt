package se.joelbit.dialife.framework

import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.framework.RoomDbEntryConverter.toDbEntry
import se.joelbit.dialife.framework.RoomDbEntryConverter.toDomainEntity
import se.joelbit.dialife.framework.db.DiaryEntriesDao
import se.joelbit.dialife.framework.db.DiaryEntriesEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class RoomDiaryEntries(val dao: DiaryEntriesDao) : DiaryEntryDataSource {
    override suspend fun add(entry: DiaryEntry) {
        val nextId = dao.getNextId()
        val dbEntry = entry.toDbEntry(nextId)
        dao.insert(dbEntry)
    }

    override suspend fun getAll(): List<DiaryEntry> {
        val dbEntries = dao.getAll()
        return dbEntries.map { it.toDomainEntity() }
    }

    override suspend fun remove(entry: DiaryEntry) {
        val dbEntry = entry.toDbEntry()
        dao.deleteOne(dbEntry)
    }
}

// Since this "mapper" is only for the room data source, there is no need to create a general mapper.
// Also, there's no good way to abstract the dao at this level, so let's keep the whole Room db structure as one conceptual unit.
object RoomDbEntryConverter {
    fun DiaryEntriesEntity.toDomainEntity() =
        DiaryEntry(
            id = id,
            title = title,
            text = text,
            datetime = LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(0)),
            icon = Icon.fromOrdinal(iconRes),
        )

    fun DiaryEntry.toDbEntry() = toDbEntry(id)
    fun DiaryEntry.toDbEntry(id: Long) =
        DiaryEntriesEntity(
            id = id,
            title = title,
            text = text,
            timestamp = datetime.toEpochSecond(ZoneOffset.ofHours(0)),
            iconRes = icon.ordinal,
        )
}