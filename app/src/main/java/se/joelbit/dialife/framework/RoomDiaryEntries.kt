package se.joelbit.dialife.framework

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.framework.RoomDbEntryConverter.toDbEntry
import se.joelbit.dialife.framework.RoomDbEntryConverter.toDomainEntity
import se.joelbit.dialife.framework.db.DiaryEntriesDao
import se.joelbit.dialife.framework.db.DiaryEntriesEntity
import se.joelbit.dialife.framework.db.DiaryEntriesEntityId
import java.time.LocalDateTime
import java.time.ZoneOffset

class RoomDiaryEntries(val dao: DiaryEntriesDao) : DiaryEntryDataSource {
    override suspend fun add(entry: DiaryEntry) {
        val nextId = dao.getNextId()
        val dbEntry = entry.toDbEntry(nextId)
        dao.insert(dbEntry)
    }

    override fun getAll() =
        dao.getAll()
            .map { list ->
                list.map {
                    it.toDomainEntity()
                }
            }

    override suspend fun update(entry: DiaryEntry) {
        dao.update(entry.toDbEntry())
    }

    override suspend fun remove(id: Long) {
        dao.deleteOne(DiaryEntriesEntityId(id))
    }
}

// Since this "mapper" is only for the room internal data source, there is no need to create a general mapper to get injected.
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