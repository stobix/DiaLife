package se.joelbit.dialife.framework.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "diary_entries")
data class DiaryEntriesEntity(
    @PrimaryKey var id: Long = 0,
    var title: String?,
    var text: String,
    @ColumnInfo(defaultValue = "1")
    var timestamp: Long,
    @ColumnInfo(defaultValue = "1")
    var iconRes: Int ,
)

data class DiaryEntriesEntityId(
    var id: Long = 0,
)
