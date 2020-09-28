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
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
import org.mockito.ArgumentMatchers

@RunWith(JUnit4::class)
class ListCompletedChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val completedChallengeRepository = mock<CompletedChallengeRepository>()

    private lateinit var  completedChallengeViewModel: ListCompletedChallengeViewModel
    @Before
    fun init(){
        completedChallengeViewModel = ListCompletedChallengeViewModel(completedChallengeRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list completed challenges success`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(totalPages = 1,totalItems = 1,data = arrayListOf(
            CompletedChallenge(completedAt = 0, id = "id")
        ))

        `when`(completedChallengeRepository.getCompletedChallenges(username,1)).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        verify(observerLoading).onChanged(true)
        verify(completedChallengeRepository).getCompletedChallenges(username,1)
        verify(observerData).onChanged(repositoryResponse.data)
        verify(observerLoading).onChanged(false)
    }


}