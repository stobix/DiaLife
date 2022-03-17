package se.joelbit.dialife.framework.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    entities = [(DiaryEntriesEntity::class)],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1,to = 2)
    ],
    exportSchema = true,
)
abstract class DiaryEntriesDb: RoomDatabase() {
    abstract fun dao(): DiaryEntriesDao
}