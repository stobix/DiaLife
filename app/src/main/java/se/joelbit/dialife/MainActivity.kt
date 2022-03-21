package se.joelbit.dialife

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryDataSource
import se.joelbit.dialife.data.OpenDiaryEntryRepository
import se.joelbit.dialife.databinding.ActivityMainBinding
import se.joelbit.dialife.framework.InMemoryDiaryEntries
import se.joelbit.dialife.framework.InMemoryOpenDiaryEntry
import se.joelbit.dialife.framework.InMemoryPredefinedDiaryEntries
import se.joelbit.dialife.framework.RoomDiaryEntries
import se.joelbit.dialife.framework.db.DiaryEntriesDb
import se.joelbit.dialife.visual.ui.diaryEntries.DiaryEntriesViewModel
import se.joelbit.dialife.visual.displayEntities.mappers.*
import se.joelbit.dialife.visual.ui.entryManagement.EntryManagementViewModel
import se.joelbit.dialife.useCases.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    data class UseCases(
        val addEntry: AddEntry,
        val removeEntry: RemoveEntry,
        val getEntries: GetEntries,

        val setOpenEntry: SetOpenEntry,
        val getOpenEntry: GetOpenEntry,
        val clearOpenEntry: ClearOpenEntry
    )

    val useCasesDef = module {
        single { DiaryEntryRepository(get()) }
        single { OpenDiaryEntryRepository(get()) }

        single { AddEntry(get()) }
        single { RemoveEntry(get()) }
        single { GetEntries(get()) }

        single { SetOpenEntry(get()) }
        single { GetOpenEntry(get()) }
        single { ClearOpenEntry(get()) }

        single { UseCases(get(), get(), get(), get(), get(), get()) }
    }


    val viewModelsDef = module {

        single<DisplayDiaryEntryMapper> {
            IconDisplayDiaryEntryMapper(get())
        }
        single<DisplayOpenDiaryEntryMapper> {
            DisplayOpenDiaryEntryMapperImpl(get())
        }

        viewModel {
            DiaryEntriesViewModel(get(), get(), get())
        }
        viewModel {
            EntryManagementViewModel(get(),get())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)

            print("hello")
            // Change to one of these to change the data source.
//            val datasourceDef= KoinInjectionDefs.inMemorydataSourcesPreDef
//            val datasourceDef= KoinInjectionDefs.inMemorydataSourcesDef
            val datasourceDef= KoinInjectionDefs.roomDataSourceDef

            // Change to one of these to change the icon resource definitions.
            val iconResDef = KoinInjectionDefs.iconResDef1
//            val iconResDef = KoinInjectionDefs::iconResDef2

            modules(
                viewModelsDef,
                datasourceDef,
                useCasesDef,
                iconResDef,
            )
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_diary_entries, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}

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
        single { Room.databaseBuilder(get(),DiaryEntriesDb::class.java,"example-injected-db").build().dao() }
        single<DiaryEntryDataSource> { RoomDiaryEntries(get()) }
        single<OpenDiaryEntryDataSource> { InMemoryOpenDiaryEntry() }
    }

    val iconResDef1 = module {
        single<DisplayIconMapper> {
            DisplayIconMapperImpl1()
        }
    }

    val iconResDef2 = module {
        single<DisplayIconMapper> {
            DisplayIconMapperImpl2()
        }
    }

}