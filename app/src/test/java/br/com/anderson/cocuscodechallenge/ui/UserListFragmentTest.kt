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
import androidx.navigation.Navigation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.RecyclerViewMatcher
import br.com.anderson.cocuscodechallenge.ViewModelUtil
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.viewmodel.ListUserViewModel
import org.junit.Before
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class,qualifiers = "w360dp-h880dp-xhdpi" )
class UserListFragmentTest {

    lateinit var testviewModel: ListUserViewModel

    lateinit var factory:FragmentFactory

    val mockNavController = Mockito.mock(NavController::class.java)

    @Before
    fun setup(){
        testviewModel = Mockito.mock(ListUserViewModel::class.java)
        factory = object : FragmentFactory(){
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return  ListUserFragment().apply {
                    this.viewModel = testviewModel
                }
            }
        }
    }


    @Test fun `test list user ui recycleview list click list`() {
        val liveDataListUser = MutableLiveData<List<User>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        Mockito.`when`(testviewModel.dataListLastUsers).thenReturn(liveDataListUser)
        Mockito.`when`(testviewModel.loading).thenReturn(loading)
        Mockito.`when`(testviewModel.message).thenReturn(message)
        Mockito.`when`(testviewModel.retry).thenReturn(retry)

        liveDataListUser.value = arrayListOf(User(datetime = 0, clan = "clan", honor = 100, leaderboardPosition = 1,name = "Name", username = "username"))
        val  scenario = launchFragmentInContainer<ListUserFragment>(themeResId = R.style.AppTheme, factory = factory)

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }

        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(isDisplayed()))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(hasDescendant(withText("username"))))
        onView(withText("username")).perform(ViewActions.click())

        Mockito.verify(mockNavController).navigate(ListUserFragmentDirections.actionListUserFragmentToUserDetailFragment("username"))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)

    }



    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycleview)
    }



}
