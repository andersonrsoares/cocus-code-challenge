package br.com.anderson.cocuscodechallenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [br.com.anderson.cocuscodechallenge.testing documentation](http://d.android.com/tools/br.com.anderson.cocuscodechallenge.testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("br.com.anderson.cocuscodechallenge", appContext.packageName)
    }
}
