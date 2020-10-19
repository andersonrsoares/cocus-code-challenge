package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
import br.com.anderson.cocuscodechallenge.ui.listcompleted.ListCompletedChallengeViewModel
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
class ListCompletedChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val completedChallengeRepository = mock<CompletedChallengeRepository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var completedChallengeViewModel: ListCompletedChallengeViewModel

    @Before
    fun init() {
        completedChallengeViewModel =
            ListCompletedChallengeViewModel(
                completedChallengeRepository
            )
        completedChallengeViewModel.resourceProvider = resourceProvider
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list completed challenges success`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(
            totalPages = 1, totalItems = 1,
            data = arrayListOf(
                CompletedChallenge(completedAt = 0, id = "id")
            )
        )

        given(completedChallengeRepository.getCompletedChallenges(username, 1)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        then(observerLoading).should().onChanged(true)
        then(completedChallengeRepository).should().getCompletedChallenges(username, 1)
        then(observerData).should().onChanged(repositoryResponse.data)
        then(observerLoading).should(times(2)).onChanged(false)
    }

    @Test
    fun `list completed challenges success data empty`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(totalPages = 1, totalItems = 1, data = arrayListOf())

        given(completedChallengeRepository.getCompletedChallenges(username, 1)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        then(observerLoading).should().onChanged(true)
        then(completedChallengeRepository).should().getCompletedChallenges(username, 1)
        then(observerData).should(org.mockito.BDDMockito.never()).onChanged(null)
        then(observerLoading).should(times(2)).onChanged(false)
    }

    @Test
    fun `list completed challenges success page 2`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(totalPages = 2, totalItems = 1, data = arrayListOf(CompletedChallenge(completedAt = 0, id = "id")))

        given(completedChallengeRepository.getCompletedChallenges(username, 1)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        given(completedChallengeRepository.getCompletedChallenges(username, 2)).willReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        given(resourceProvider.getString(ArgumentMatchers.anyInt())).willReturn("end of list")

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observerMessage = mock<Observer<String>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.message.observeForever(observerMessage)

        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        then(observerLoading).should().onChanged(true)
        then(completedChallengeRepository).should().getCompletedChallenges(username, 1)
        then(observerData).should().onChanged(repositoryResponse.data)
        then(observerLoading).should(times(2)).onChanged(false)
        completedChallengeViewModel.listScrolled(2, 3, 5)
        then(observerLoading).should(times(2)).onChanged(true)
        then(observerData).should().onChanged(repositoryResponse.data)
        then(observerLoading).should(times(4)).onChanged(false)
        then(observerMessage).should().onChanged("end of list")
    }
}
