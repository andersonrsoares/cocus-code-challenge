package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.concurrent.Callable


@RunWith(JUnit4::class)
class ListUserViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository = mock<UserRepository>()


    private lateinit var  userViewModel: ListUserViewModel
    @Before
    fun init(){
        userViewModel = ListUserViewModel(userRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }



    @Test
    fun `list last user searched`() {

        val username = "baz"

        val repositoryResponse = listOf(User(datetime = 0,username = username))



        `when`(userRepository.listLastUsers()).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<User>>>()
        val observerLoading = mock<Observer<Boolean>>()

        userViewModel.loading.observeForever(observerLoading)
        userViewModel.listLastUsers().observeForever(observerData)
        verify(observerLoading).onChanged(true)
        verify(userRepository).listLastUsers()
        verify(observerData).onChanged(repositoryResponse)
        verify(observerLoading).onChanged(false)
    }





}