package br.com.anderson.cocuscodechallenge.ui

import android.app.Application
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.RecyclerViewMatcher
import br.com.anderson.cocuscodechallenge.ViewModelUtil
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.ui.listuser.ListUserFragment
import br.com.anderson.cocuscodechallenge.ui.listuser.ListUserFragmentDirections
import br.com.anderson.cocuscodechallenge.ui.listuser.ListUserViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class, qualifiers = "w360dp-h880dp-xhdpi")
class UserListFragmentTest {

    lateinit var testviewModel: ListUserViewModel

    lateinit var factory: FragmentFactory

    val mockNavController = Mockito.mock(NavController::class.java)

    @Before
    fun setup() {
        testviewModel = Mockito.mock(ListUserViewModel::class.java)
        factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return ListUserFragment()
                    .apply {
                        this.factory = ViewModelUtil.createFor(testviewModel)
                    }
            }
        }
    }

    @Test fun `test list user ui recycleview list click list`() {
        val liveDataListUser = MutableLiveData<List<User>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val newUser = MutableLiveData<Boolean>()
        val empty = MutableLiveData<Boolean>()
        val clean = MutableLiveData<Boolean>()
        given(testviewModel.dataListLastUsers).willReturn(liveDataListUser)
        given(testviewModel.loading).willReturn(loading)
        given(testviewModel.message).willReturn(message)
        given(testviewModel.retry).willReturn(retry)
        given(testviewModel.newUser).willReturn(newUser)
        given(testviewModel.empty).willReturn(empty)
        given(testviewModel.clean).willReturn(clean)

        liveDataListUser.value = arrayListOf(User(datetime = 0, clan = "clan", honor = 100, leaderboardPosition = 1, name = "Name", username = "username"))
        val scenario = launchFragmentInContainer<ListUserFragment>(themeResId = R.style.AppTheme, factory = factory)

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }

        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("username"))))
        onView(ViewMatchers.withText("username")).perform(ViewActions.click())

        Mockito.verify(mockNavController).navigate(ListUserFragmentDirections.actionListUserFragmentToUserDetailFragment("username"))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycleview)
    }
}
