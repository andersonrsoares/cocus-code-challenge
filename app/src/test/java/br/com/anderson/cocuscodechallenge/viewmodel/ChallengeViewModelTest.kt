package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.repository.ChallengeRepository
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times

@RunWith(JUnit4::class)
class ChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val challengeRepository = mock<ChallengeRepository>()

    private lateinit var challengeViewModel: ChallengeViewModel
    @Before
    fun init() {
        challengeViewModel = ChallengeViewModel(challengeRepository)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list authored challenges success`() {

        val id = "id"

        val repositoryResponse = Challenge(id = "id")
        //given
        given(challengeRepository.getChallenge(id)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<Challenge>>()
        val observerLoading = mock<Observer<Boolean>>()

        //when
        challengeViewModel.loading.observeForever(observerLoading)
        challengeViewModel.dataChallenge.observeForever(observerData)
        challengeViewModel.listChallenge(id)

        //then
        then(challengeRepository)
            .should().getChallenge(id)

        then(observerLoading)
            .should().onChanged(true)

        then(observerData)
            .should().onChanged(repositoryResponse)

        then(observerLoading)
            .should(times(2)).onChanged(false)

    }

    @Test
    fun `list authored challenges data empty`() {

        val id = "id"
        given(challengeRepository.getChallenge(id)).willReturn(Flowable.empty())

        val observerData = mock<Observer<Challenge>>()
        val observerLoading = mock<Observer<Boolean>>()

        challengeViewModel.loading.observeForever(observerLoading)
        challengeViewModel.dataChallenge.observeForever(observerData)
        challengeViewModel.listChallenge(id)

        then(observerLoading).should().onChanged(true)
        then(challengeRepository).should().getChallenge(id)
        then(observerData).shouldHaveZeroInteractions()
        then(observerLoading).should().onChanged(false)
    }
}
