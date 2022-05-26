package se.joelbit.dialife.framework.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface  DiaryEntriesDao {
    @Query("select * from diary_entries")
    fun getAll(): Flow<List<DiaryEntry>>

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

    @Insert
    suspend fun insert(picture: Picture): Long

    @Insert
    suspend fun insert(picture: Diary2Pictures)

    @Query("delete from `diary entries <-> pictures` where entryId == :diaryEntryId")
    suspend fun deleteAllPicturesFor(diaryEntryId: Int)

    @Insert
    suspend fun update(picture: Picture)

}