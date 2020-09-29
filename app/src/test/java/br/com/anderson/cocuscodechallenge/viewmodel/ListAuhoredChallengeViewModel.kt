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
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.repository.AuthoredChallengeRepository
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
import org.mockito.ArgumentMatchers

@RunWith(JUnit4::class)
class ListAuthoredChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val authoredChallengeRepository = mock<AuthoredChallengeRepository>()

    private lateinit var  auhtoredChallengeViewModel: ListAuthoredChallengeViewModel
    @Before
    fun init(){
        auhtoredChallengeViewModel = ListAuthoredChallengeViewModel(authoredChallengeRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list authored challenges success`() {

        val username = "baz"

        val repositoryResponse = arrayListOf(AuthoredChallenge(id = "id"))

        `when`(authoredChallengeRepository.getAuthoredChallenges(username)).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<AuthoredChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataAuthoredChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listUserAuthoredChallenge(username)
        verify(observerLoading).onChanged(true)
        verify(authoredChallengeRepository).getAuthoredChallenges(username)
        verify(observerData).onChanged(repositoryResponse)
        verify(observerLoading).onChanged(false)
    }

    @Test
    fun `list authored challenges data empty`() {

        val username = "baz"

        val repositoryResponse = arrayListOf<AuthoredChallenge>()

        `when`(authoredChallengeRepository.getAuthoredChallenges(username)).thenReturn(Flowable.just(repositoryResponse))

        val observerData = mock<Observer<List<AuthoredChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataAuthoredChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listUserAuthoredChallenge(username)
        verify(observerLoading).onChanged(true)
        verify(authoredChallengeRepository).getAuthoredChallenges(username)
        verify(observerData, never()).onChanged(repositoryResponse)
        verify(observerLoading).onChanged(false)
    }

}