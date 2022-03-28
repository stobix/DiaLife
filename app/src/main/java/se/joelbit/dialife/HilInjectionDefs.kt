package se.joelbit.dialife

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryDataSource
import se.joelbit.dialife.data.OpenDiaryEntryRepository
import se.joelbit.dialife.framework.InMemoryDiaryEntries
import se.joelbit.dialife.framework.InMemoryOpenDiaryEntry
import se.joelbit.dialife.framework.RoomDiaryEntries
import se.joelbit.dialife.framework.db.DiaryEntriesDao
import se.joelbit.dialife.framework.db.DiaryEntriesDb
import se.joelbit.dialife.network.ktorServer.KtorApi
import se.joelbit.dialife.network.ktorServer.KtorDiaryEntries
import se.joelbit.dialife.network.ktorServer.KtorRetrofitReceiver
import se.joelbit.dialife.network.ktorServer.NetworkDiaryEntryMapper
import se.joelbit.dialife.useCases.*
import se.joelbit.dialife.visual.displayEntities.mappers.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InMemoryDataSource {
    @Provides
    @Named("mem-source")
    fun source(): DiaryEntryDataSource = InMemoryDiaryEntries()
}

@Module
@InstallIn(SingletonComponent::class)
object RoomDataSource {
    @Provides
    @Named("room-source")
    fun source(dao: DiaryEntriesDao): DiaryEntryDataSource = RoomDiaryEntries(dao)

    @Provides fun dao(@ApplicationContext ctx: Context): DiaryEntriesDao =
        Room.databaseBuilder(ctx, DiaryEntriesDb::class.java, "example-injected-db").build()
        .dao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSource {
    @Provides
    @Named("network-source")
    fun source( mapper: NetworkDiaryEntryMapper, retrofitApi: KtorApi): DiaryEntryDataSource = KtorDiaryEntries(mapper, retrofitApi)

    @Provides
    fun retrofitApi() = KtorRetrofitReceiver.getApiInstance(KtorApi::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
object UseCasesKtor {
    @Provides
    @Named("ktorUseCases")
    fun getEntries(
        @Named("ktor-repo")
        repository: DiaryEntryRepository) = GetEntries(repository)

    @Provides
    @Named("ktorUseCases")
    fun addEntry(
        @Named("ktor-repo")
        repository: DiaryEntryRepository) = AddEntry(repository)

    @Provides
    @Named("ktorUseCases")
    fun updateEntry(
        @Named("ktor-repo")
        repository: DiaryEntryRepository) = UpdateEntry(repository)

    @Provides
    @Named("ktorUseCases")
    fun removeEntry(
        @Named("ktor-repo")
        repository: DiaryEntryRepository) = RemoveEntry(repository)
}

@Module
@InstallIn(SingletonComponent::class)
object Mappers {
    @Provides fun iconMapper(): DisplayIconMapper = DisplayIconSmiliesMapper()

    @Provides fun diaryEntryMapper(iconMapper: DisplayIconMapper): DisplayDiaryEntryMapper = IconDisplayDiaryEntryMapper(iconMapper)

    @Provides fun openDiaryEntryMapper(entryMapper: DisplayDiaryEntryMapper): DisplayOpenDiaryEntryMapper = DisplayOpenDiaryEntryMapperImpl(entryMapper)

    @Provides fun networkMapper() = NetworkDiaryEntryMapper()

}

@Module
@InstallIn(SingletonComponent::class)
object InMemoryOpenDataSource {
    @Provides fun source() : OpenDiaryEntryDataSource = InMemoryOpenDiaryEntry()
}

@Module
@InstallIn(SingletonComponent::class)
object Repositories {
    @Provides
    @Singleton
    fun entryRepository(
//        @Named("room-source")
        @Named("network-source")
        source: DiaryEntryDataSource
    ): DiaryEntryRepository = DiaryEntryRepository(source)

    @Provides
    @Singleton
    @Named("ktor-repo")
    fun ktorRepository(
        @Named("room-source")
        source: DiaryEntryDataSource
    ): DiaryEntryRepository = DiaryEntryRepository(source)
    @Provides
    @Singleton
    fun openEntryRepository(source: OpenDiaryEntryDataSource): OpenDiaryEntryRepository = OpenDiaryEntryRepository(source)
}

/**
 * Using a module for UseCases since I want to keep the inner layers clean of Hilt annotations.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCases {
    @Provides fun getEntries(repository: DiaryEntryRepository) = GetEntries(repository)
    @Provides fun addEntry(repository: DiaryEntryRepository) = AddEntry(repository)
    @Provides fun updateEntry(repository: DiaryEntryRepository) = UpdateEntry(repository)
    @Provides fun removeEntry(repository: DiaryEntryRepository) = RemoveEntry(repository)
    @Provides fun getOpenEntry(repository: OpenDiaryEntryRepository) = GetOpenEntry(repository)
    @Provides fun clearOpenEntry(repository: OpenDiaryEntryRepository) = ClearOpenEntry(repository)
    @Provides fun setOpenEntry(repository: OpenDiaryEntryRepository) = SetOpenEntry(repository)
}


