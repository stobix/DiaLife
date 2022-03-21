package se.joelbit.dialife

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module


import org.koin.test.KoinTest
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class OverrideDbTest : KoinTest {

    val overridden = module(override = true) {
        single { KoinInjectionDefs.iconResDef1  }
    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("se.joelbit.dialife", appContext.packageName)
    }

    @BeforeTest
    fun before() {
        loadKoinModules(overridden)
    }

    @AfterTest
    fun after() {
        unloadKoinModules(overridden)
    }
}