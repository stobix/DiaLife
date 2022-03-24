package se.joelbit.dialife

import androidx.room.Room
import org.koin.core.qualifier.named
import org.koin.dsl.module
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryDataSource
import se.joelbit.dialife.framework.InMemoryDiaryEntries
import se.joelbit.dialife.framework.InMemoryOpenDiaryEntry
import se.joelbit.dialife.framework.InMemoryPredefinedDiaryEntries
import se.joelbit.dialife.framework.RoomDiaryEntries
import se.joelbit.dialife.framework.db.DiaryEntriesDb
import se.joelbit.dialife.network.ktorServer.*
import se.joelbit.dialife.useCases.AddEntry
import se.joelbit.dialife.useCases.GetEntries
import se.joelbit.dialife.useCases.RemoveEntry
import se.joelbit.dialife.useCases.UpdateEntry
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconSmiliesMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconHandsMapper

object KoinInjectionDefs {
    val inMemorydataSourcesPreDef = module {
        single<DiaryEntryDataSource> { InMemoryPredefinedDiaryEntries() }
        single<OpenDiaryEntryDataSource> { InMemoryOpenDiaryEntry() }
    }

    val inMemorydataSourcesDef = module {
        single<DiaryEntryDataSource> { InMemoryDiaryEntries() }
        single<OpenDiaryEntryDataSource> { InMemoryOpenDiaryEntry() }
    }

    val roomDataSourceDef = module {
        single {
            Room.databaseBuilder(get(), DiaryEntriesDb::class.java, "example-injected-db").build()
                .dao()
        }
        single<DiaryEntryDataSource> { RoomDiaryEntries(get()) }
        single<OpenDiaryEntryDataSource> { InMemoryOpenDiaryEntry() }
    }

    val ktorServerDef= module {

        //

        single(named("ktorUseCase")) {
            Room.databaseBuilder(get(), DiaryEntriesDb::class.java, "example-injected-db").build()
                .dao()
        }
        single<DiaryEntryDataSource>(named("ktorUseCase")) { RoomDiaryEntries(get(named("ktorUseCase"))) }

        single(named("ktorUseCase")) { DiaryEntryRepository(get(named("ktorUseCase"))) }

        single(named("ktorUseCase")) { AddEntry(get(named("ktorUseCase"))) }
        single(named("ktorUseCase")) { RemoveEntry(get(named("ktorUseCase"))) }
        single(named("ktorUseCase")) { UpdateEntry(get(named("ktorUseCase"))) }
        single(named("ktorUseCase")) { GetEntries(get(named("ktorUseCase"))) }

        // Mapper for the network data
        single { NetworkDiaryEntryMapper() }

        // embedded KTor server that has UseCases communicating with the db
        single { KtorUseCases(get(named("ktorUseCase")), get(named("ktorUseCase")), get(named("ktorUseCase")), get(named("ktorUseCase"))) }
        single { KtorDbProvider(get(), get()) }
    }

    val ktorDataSourceDef = module {

        // retrofit receiver communicating with the internal server
        single { KtorRetrofitReceiver.getApiInstance(KtorApi::class.java) }
        single<DiaryEntryDataSource> { KtorDiaryEntries(get(), get()) }
        single<OpenDiaryEntryDataSource> { InMemoryOpenDiaryEntry() }

    }

    val iconResDef1 = module {
        single<DisplayIconMapper> {
            DisplayIconSmiliesMapper()
        }
    }

    val iconResDef2 = module {
        single<DisplayIconMapper> {
            DisplayIconHandsMapper()
        }
    }

}