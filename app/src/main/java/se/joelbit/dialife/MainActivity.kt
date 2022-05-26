package se.joelbit.dialife

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryRepository
import se.joelbit.dialife.databinding.ActivityMainBinding
import se.joelbit.dialife.network.ktorServer.KtorDbProvider
import se.joelbit.dialife.useCases.*
import se.joelbit.dialife.visual.displayEntities.mappers.*
import se.joelbit.dialife.visual.ui.diaryEntries.DiaryEntriesViewModel
import se.joelbit.dialife.visual.ui.entryManagement.EntryCreationViewModel
import se.joelbit.dialife.visual.ui.settings.SettingsViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel by viewModel<MainViewModel>()

    val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when(it) {
                true -> Log.d("Camera", "YES")
                false -> Log.d("Camera", "No no...")
            }
            viewModel.hasCamPerm.value = it
        }

    fun getCam() {
        Manifest.permission.CAMERA.let { perm ->
            when (ContextCompat.checkSelfPermission(this, perm)){
                PackageManager.PERMISSION_GRANTED -> {
                    Log.d("Camera", "Camera prev granted")
                    viewModel.hasCamPerm.value = true
                }
                else -> when {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, perm) -> {
                        Log.d("Camera", "Show dialog thing")
                        requestCamera.launch(perm)
                    }
                    else ->
                        requestCamera.launch(perm)
                }
            }
        }
    }

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
            IconDisplayDiaryEntryMapper(get(), get())
        }

        single<PictureMapper> {
            PictureMapperImpl()
        }

        single<DisplayOpenDiaryEntryMapper> {
            DisplayOpenDiaryEntryMapperImpl(get())
        }

        viewModelOf ( ::DiaryEntriesViewModel )
        viewModelOf ( ::EntryCreationViewModel )
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

        getCam()
        viewModel.mediaDir.value = getMediaDir()


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

    fun getMediaDir(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return when {
            mediaDir?.exists() == true -> mediaDir
            else -> filesDir
        }
    }
}
