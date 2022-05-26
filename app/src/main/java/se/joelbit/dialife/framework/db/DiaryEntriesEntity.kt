package se.joelbit.dialife.framework.db

import androidx.room.*


@Entity(tableName = "diary_entries")
data class DiaryEntriesEntity(
    @PrimaryKey val id: Long = 0,
    val title: String?,
    val text: String,
    @ColumnInfo(defaultValue = "1")
    val timestamp: Long,
    @ColumnInfo(defaultValue = "1")
    val iconRes: Int ,
    val iconRef: String? = null,
)

data class DiaryEntriesEntityId(
    val id: Long = 0,
)


@Entity(tableName = "pictures")
data class Picture(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val uri: String,
    val timestamp: Long,
)

@Entity(tableName = "diary entries <-> pictures",
    primaryKeys = ["picId", "entryId"] )
data class Diary2Pictures(
    val picId: Long,
    val entryId: Long,
)

data class DiaryEntry(
    @Embedded val entry: DiaryEntriesEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = Picture::class,
        associateBy = Junction(
            value = Diary2Pictures::class,
            parentColumn = "entryId",
            entityColumn = "picId"
        )
    )
    val pictures: List<Picture>
)
