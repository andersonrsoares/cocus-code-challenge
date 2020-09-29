package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
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
import org.mockito.Mockito.*
import br.com.anderson.cocuscodechallenge.any
import org.mockito.ArgumentMatchers

@RunWith(JUnit4::class)
class ListUserViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository = mock<UserRepository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var  userViewModel: ListUserViewModel
    @Before
    fun init(){
        userViewModel = ListUserViewModel(resourceProvider,userRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }



    @Test
    fun `list last user searched success`() {

        val username = "baz"

        val repositoryResponse = listOf(User(datetime = 0,username = username))

        `when`(userRepository.listLastUsers()).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.listLastUsers()
        verify(observerLoading).onChanged(true)
        verify(userRepository).listLastUsers()
        verify(observerData).onChanged(repositoryResponse)
        verify(observerLoading).onChanged(false)
    }


    @Test
    fun `search user by name success`() {

        val username = "baz"

        val repositoryResponse = User(datetime = 0,username = username)

        `when`(userRepository.searchUser(username)).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.dataListLastUsers.observeForever(observerData)
        userViewModel.searchUser(username)
        verify(observerLoading).onChanged(true)
        verify(userRepository).searchUser(username)
        verify(observerData).onChanged(listOf(repositoryResponse))
        verify(observerLoading, times(2)).onChanged(false)
    }

    @Test
    fun `search user by name empty`() {

        `when`(resourceProvider.getString(ArgumentMatchers.anyInt())).thenReturn("message")

        val observerMessage = mock<Observer<String>>()

        userViewModel.message.observeForever(observerMessage)

        userViewModel.searchUser("")
        verify(observerMessage).onChanged("message")

    }

}