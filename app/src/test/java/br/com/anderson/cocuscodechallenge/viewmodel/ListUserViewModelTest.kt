package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times

@RunWith(JUnit4::class)
class ListUserViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository = mock<UserRepository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var userViewModel: ListUserViewModel
    @Before
    fun init() {
        userViewModel = ListUserViewModel(userRepository)
        userViewModel.resourceProvider = resourceProvider
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list last user searched success`() {

        val username = "baz"

        val repositoryResponse = listOf(User(datetime = 0, username = username))

        given(userRepository.listLastUsers()).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.listLastUsers()
        then(observerLoading).should().onChanged(true)
        then(userRepository).should().listLastUsers()
        then(observerData).should().onChanged(repositoryResponse)
        then(observerLoading).should().onChanged(false)
    }

    @Test
    fun `search user by name success`() {

        val username = "baz"

        val repositoryResponse = User(datetime = 0, username = username)

        given(userRepository.searchUser(username)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.searchUser(username)
        then(observerLoading).should().onChanged(true)
        then(userRepository).should().searchUser(username)
        then(observerData).should().onChanged(listOf(repositoryResponse))
        then(observerLoading).should(times(2)).onChanged(false)
    }

    @Test
    fun `search user by name empty`() {

        given(resourceProvider.getString(ArgumentMatchers.anyInt())).willReturn("message")

        val observerMessage = mock<Observer<String>>()

        userViewModel.message.observeForever(observerMessage)

        userViewModel.searchUser("")
        then(observerMessage).should().onChanged("message")
    }
}
