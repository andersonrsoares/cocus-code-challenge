package br.com.anderson.cocuscodechallenge.ui


import android.os.Build
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.RecyclerViewMatcher
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.viewmodel.ChallengeViewModel
import br.com.anderson.cocuscodechallenge.viewmodel.ListAuthoredChallengeViewModel
import org.junit.Before
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class,qualifiers = "w360dp-h880dp-xhdpi" )
class ChallengeFragmentFragmentTest {

    lateinit var testviewModel: ChallengeViewModel

    lateinit var factory:FragmentFactory

    @Before
    fun setup(){
        testviewModel = Mockito.mock(ChallengeViewModel::class.java)
        factory = object : FragmentFactory(){
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return ChallengeFragment().apply {
                    this.viewModel = testviewModel
                }
            }
        }
    }


    @Test fun `test challenge ui`() {
        val liveDataListUser = MutableLiveData<Challenge>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val clean = MutableLiveData<Boolean>()
        Mockito.`when`(testviewModel.dataChallenge).thenReturn(liveDataListUser)
        Mockito.`when`(testviewModel.loading).thenReturn(loading)
        Mockito.`when`(testviewModel.message).thenReturn(message)
        Mockito.`when`(testviewModel.retry).thenReturn(retry)
        Mockito.`when`(testviewModel.clean).thenReturn(clean)


        liveDataListUser.value = Challenge(  name = "Name", description = "description", id = "id")
        val  scenario = launchFragmentInContainer<ChallengeFragment>(themeResId = R.style.AppTheme, factory = factory)

        scenario.onFragment {

        }

        onView(withText("description")).check(ViewAssertions.matches(isDisplayed()))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)

    }




}
