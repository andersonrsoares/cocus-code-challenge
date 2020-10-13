package br.com.anderson.cocuscodechallenge.ui

import android.app.Application
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.RecyclerViewMatcher
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.viewmodel.ListAuthoredChallengeViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class, qualifiers = "w360dp-h880dp-xhdpi")
class ListAuthoredChallengeFragmentFragmentTest {

    lateinit var testviewModel: ListAuthoredChallengeViewModel

    lateinit var factory: FragmentFactory

    @Before
    fun setup() {
        testviewModel = Mockito.mock(ListAuthoredChallengeViewModel::class.java)
        factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return ListAuthoredChallengeFragment().apply {
                    this.viewModel = testviewModel
                }
            }
        }
    }

    @Test fun `test authored challenge ui recycleview list`() {
        val liveDataListAuthored = MutableLiveData<List<AuthoredChallenge>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val clean = MutableLiveData<Boolean>()
        Mockito.`when`(testviewModel.dataAuthoredChallenge).thenReturn(liveDataListAuthored)
        Mockito.`when`(testviewModel.loading).thenReturn(loading)
        Mockito.`when`(testviewModel.message).thenReturn(message)
        Mockito.`when`(testviewModel.retry).thenReturn(retry)
        Mockito.`when`(testviewModel.clean).thenReturn(clean)

        liveDataListAuthored.value = arrayListOf(AuthoredChallenge(rankName = "rankName", name = "Name", username = "username"))
        val scenario = launchFragmentInContainer<ListAuthoredChallengeFragment>(themeResId = R.style.AppTheme, factory = factory)

        scenario.onFragment {
        }
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(isDisplayed()))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycleview)
    }
}
