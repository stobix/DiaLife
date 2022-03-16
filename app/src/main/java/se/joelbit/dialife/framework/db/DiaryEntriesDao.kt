package se.joelbit.dialife.framework.db

import androidx.room.*

@Dao
interface  DiaryEntriesDao {
    @Query("select * from diary_entries")
    suspend fun getAll(): List<DiaryEntriesEntity>

    @Insert
    suspend fun insertAll(entriesList: List<DiaryEntriesEntity>)

    @Insert
    suspend fun insert(entry: DiaryEntriesEntity)

    @Update
    suspend fun updateAll(entriesList: List<DiaryEntriesEntity>)

    @Delete
    suspend fun deleteOne(entry: DiaryEntriesEntity)

    @Query("delete from diary_entries")
    suspend fun deleteAll()
}