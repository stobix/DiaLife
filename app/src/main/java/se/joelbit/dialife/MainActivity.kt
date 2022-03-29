package se.joelbit.dialife

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryRepository
import se.joelbit.dialife.databinding.ActivityMainBinding
import se.joelbit.dialife.network.ktorServer.KtorDbProvider
import se.joelbit.dialife.useCases.*
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayOpenDiaryEntryMapperImpl
import se.joelbit.dialife.visual.displayEntities.mappers.IconDisplayDiaryEntryMapper
import se.joelbit.dialife.visual.ui.diaryEntries.DiaryEntriesViewModel
import se.joelbit.dialife.visual.ui.entryManagement.EntryManagementViewModel
import se.joelbit.dialife.visual.ui.settings.SettingsViewModel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    data class MainUseCases (
        val addEntry: AddEntry,
        val removeEntry: RemoveEntry,
        val updateEntry: UpdateEntry,
        val getEntries: GetEntries,

        val setOpenEntry: SetOpenEntry,
        val getOpenEntry: GetOpenEntry,
        val clearOpenEntry: ClearOpenEntry
    )

    val useCasesDef = module {
        singleOf ( ::DiaryEntryRepository )
        singleOf ( ::OpenDiaryEntryRepository )

        singleOf ( ::AddEntry )
        singleOf ( ::RemoveEntry )
        singleOf ( ::UpdateEntry )
        singleOf ( ::GetEntries )

        singleOf ( ::SetOpenEntry )
        singleOf ( ::GetOpenEntry )
        singleOf ( ::ClearOpenEntry )

        singleOf ( ::MainUseCases )
    }


    val viewModelsDef = module {

        single<DisplayDiaryEntryMapper> {
            IconDisplayDiaryEntryMapper(get())
        }
        single<DisplayOpenDiaryEntryMapper> {
            DisplayOpenDiaryEntryMapperImpl(get())
        }

        viewModelOf ( ::DiaryEntriesViewModel )
        viewModelOf ( ::EntryManagementViewModel )
        viewModelOf ( ::MainViewModel )
        viewModelOf ( ::SettingsViewModel )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)

            // Change to one of these to change the data source.
//            val datasourceDef= KoinInjectionDefs.inMemorydataSourcesPreDef
//            val datasourceDef= KoinInjectionDefs.inMemorydataSourcesDef
            val datasourceDef= KoinInjectionDefs.roomDataSourceDef
//            val datasourceDef= KoinInjectionDefs.ktorDataSourceDef

            // Change to one of these to change the icon resource definitions.
            val iconResDef = KoinInjectionDefs.iconResDef1
//            val iconResDef = KoinInjectionDefs.iconResDef2

            modules(
                viewModelsDef,
                datasourceDef,
                useCasesDef,
                iconResDef,
                KoinInjectionDefs.ktorServerDef
            )
        }


        val engine = get<KtorDbProvider>().engine

        thread(start=true, isDaemon = true){
            Log.d("Ktor","Initiating server")
            engine.start()
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
