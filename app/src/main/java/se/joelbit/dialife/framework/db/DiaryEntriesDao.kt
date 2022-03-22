package se.joelbit.dialife.framework.db

import androidx.room.*

@Dao
interface  DiaryEntriesDao {
    @Query("select * from diary_entries")
    suspend fun getAll(): List<DiaryEntriesEntity>

    @Query("select case when max(id) is null then 1 else max(id)+1 end from diary_entries")
    suspend fun getNextId(): Long

    @Insert
    suspend fun insertAll(entriesList: List<DiaryEntriesEntity>)

    @Insert
    suspend fun insert(entry: DiaryEntriesEntity)

    @Update
    suspend fun updateAll(entriesList: List<DiaryEntriesEntity>)

    @Update
    suspend fun update(entry: DiaryEntriesEntity)

    @Delete
    suspend fun deleteOne(entry: DiaryEntriesEntity)

    @Delete(entity = DiaryEntriesEntity::class)
    suspend fun deleteOne(id: DiaryEntriesEntityId)

    @Query("delete from diary_entries")
    suspend fun deleteAll()
}