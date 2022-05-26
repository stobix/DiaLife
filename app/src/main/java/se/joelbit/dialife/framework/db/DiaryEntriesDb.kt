package se.joelbit.dialife.framework.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DiaryEntriesEntity::class,Picture::class, Diary2Pictures::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1,to = 2),
        AutoMigration(from = 2,to = 3),
    ],
    exportSchema = true,
)
abstract class DiaryEntriesDb: RoomDatabase() {
    abstract fun dao(): DiaryEntriesDao
}