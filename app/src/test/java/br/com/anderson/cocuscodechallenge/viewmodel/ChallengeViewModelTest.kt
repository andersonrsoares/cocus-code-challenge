package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.repository.ChallengeRepository


@RunWith(JUnit4::class)
class ChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val challengeRepository = mock<ChallengeRepository>()

    private lateinit var  auhtoredChallengeViewModel: ChallengeViewModel
    @Before
    fun init(){
        auhtoredChallengeViewModel = ChallengeViewModel(challengeRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list authored challenges success`() {

        val id = "id"

        val repositoryResponse = Challenge(id = "id")

        `when`(challengeRepository.getChallenge(id)).thenReturn(Flowable.just( DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<Challenge>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listChallenge(id)
        verify(observerLoading).onChanged(true)
        verify(challengeRepository).getChallenge(id)
        verify(observerData).onChanged(repositoryResponse)
        verify(observerLoading, times(2)).onChanged(false)
    }

    @Test
    fun `list authored challenges data empty`() {

        val id = "id"
        `when`(challengeRepository.getChallenge(id)).thenReturn(Flowable.empty())

        val observerData = mock<Observer<Challenge>>()
        val observerLoading = mock<Observer<Boolean>>()

        auhtoredChallengeViewModel.loading.observeForever(observerLoading)
        auhtoredChallengeViewModel.dataChallenge.observeForever(observerData)
        auhtoredChallengeViewModel.listChallenge(id)
        verify(observerLoading).onChanged(true)
        verify(challengeRepository).getChallenge(id)
        verify(observerData, never()).onChanged(null)
        verify(observerLoading).onChanged(false)
    }

}