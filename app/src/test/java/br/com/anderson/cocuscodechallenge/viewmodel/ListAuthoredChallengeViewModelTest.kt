package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.repository.AuthoredChallengeRepository
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class ListAuthoredChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val authoredChallengeRepository = mock<AuthoredChallengeRepository>()

    private lateinit var auhtoredChallengeViewModel: ListAuthoredChallengeViewModel
    @Before
    fun init() {
        auhtoredChallengeViewModel = ListAuthoredChallengeViewModel(authoredChallengeRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list authored challenges success`() {

        val username = "baz"

        val repositoryResponse = listOf(AuthoredChallenge(id = "id"))

        Mockito.`when`(authoredChallengeRepository.getAuthoredChallenges(username)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<AuthoredChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataAuthoredChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listUserAuthoredChallenge(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(authoredChallengeRepository).getAuthoredChallenges(username)
        Mockito.verify(observerData).onChanged(repositoryResponse)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
    }

    @Test
    fun `list authored challenges data empty`() {

        val username = "baz"

        val repositoryResponse = listOf<AuthoredChallenge>()

        Mockito.`when`(authoredChallengeRepository.getAuthoredChallenges(username)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<AuthoredChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataAuthoredChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listUserAuthoredChallenge(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(authoredChallengeRepository).getAuthoredChallenges(username)
        Mockito.verify(observerData, Mockito.never()).onChanged(repositoryResponse)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
    }
}