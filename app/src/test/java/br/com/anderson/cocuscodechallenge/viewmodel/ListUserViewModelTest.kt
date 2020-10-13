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
import org.mockito.Mockito

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

        Mockito.`when`(userRepository.listLastUsers()).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.listLastUsers()
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(userRepository).listLastUsers()
        Mockito.verify(observerData).onChanged(repositoryResponse)
        Mockito.verify(observerLoading).onChanged(false)
    }

    @Test
    fun `search user by name success`() {

        val username = "baz"

        val repositoryResponse = User(datetime = 0, username = username)

        Mockito.`when`(userRepository.searchUser(username)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.searchUser(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(userRepository).searchUser(username)
        Mockito.verify(observerData).onChanged(listOf(repositoryResponse))
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
    }

    @Test
    fun `search user by name empty`() {

        Mockito.`when`(resourceProvider.getString(ArgumentMatchers.anyInt())).thenReturn("message")

        val observerMessage = mock<Observer<String>>()

        userViewModel.message.observeForever(observerMessage)

        userViewModel.searchUser("")
        Mockito.verify(observerMessage).onChanged("message")
    }
}
