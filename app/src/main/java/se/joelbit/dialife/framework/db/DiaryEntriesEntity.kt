package se.joelbit.dialife.framework.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "diary_entries")
data class DiaryEntriesEntity(
    @PrimaryKey var id: Long = 0,
    var text: String,
)

