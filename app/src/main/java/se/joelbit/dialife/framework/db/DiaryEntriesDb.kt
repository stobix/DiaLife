package se.joelbit.dialife.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(DiaryEntriesEntity::class)], version = 1)
abstract class DiaryEntriesDb: RoomDatabase() {
    abstract fun dao(): DiaryEntriesDao
}