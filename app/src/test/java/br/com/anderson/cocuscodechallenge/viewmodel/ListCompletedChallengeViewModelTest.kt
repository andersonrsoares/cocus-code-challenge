package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
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
class ListCompletedChallengeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val completedChallengeRepository = mock<CompletedChallengeRepository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var completedChallengeViewModel: ListCompletedChallengeViewModel

    @Before
    fun init() {
        completedChallengeViewModel = ListCompletedChallengeViewModel(completedChallengeRepository)
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

        Mockito.`when`(completedChallengeRepository.getCompletedChallenges(username, 1)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(completedChallengeRepository).getCompletedChallenges(username, 1)
        Mockito.verify(observerData).onChanged(repositoryResponse.data)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
    }

    @Test
    fun `list completed challenges success data empty`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(totalPages = 1, totalItems = 1, data = arrayListOf())

        Mockito.`when`(completedChallengeRepository.getCompletedChallenges(username, 1)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(completedChallengeRepository).getCompletedChallenges(username, 1)
        Mockito.verify(observerData, Mockito.never()).onChanged(repositoryResponse.data)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
    }

    @Test
    fun `list completed challenges success page 2`() {

        val username = "baz"

        val repositoryResponse = PageCompletedChallenge(totalPages = 2, totalItems = 1, data = arrayListOf(CompletedChallenge(completedAt = 0, id = "id")))

        Mockito.`when`(completedChallengeRepository.getCompletedChallenges(username, 1)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        Mockito.`when`(completedChallengeRepository.getCompletedChallenges(username, 2)).thenReturn(Flowable.just(DataSourceResult.create(repositoryResponse)))

        Mockito.`when`(resourceProvider.getString(ArgumentMatchers.anyInt())).thenReturn("end of list")

        val observerData = mock<Observer<List<CompletedChallenge>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observerMessage = mock<Observer<String>>()

        completedChallengeViewModel.loading.observeForever(observerLoading)
        completedChallengeViewModel.message.observeForever(observerMessage)

        completedChallengeViewModel.dataCompletedChallenge.observeForever(observerData)
        completedChallengeViewModel.listUserCompletedChallenge(username)
        Mockito.verify(observerLoading).onChanged(true)
        Mockito.verify(completedChallengeRepository).getCompletedChallenges(username, 1)
        Mockito.verify(observerData).onChanged(repositoryResponse.data)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(false)
        completedChallengeViewModel.listScrolled(2, 3, 5)
        Mockito.verify(observerLoading, Mockito.times(2)).onChanged(true)
        Mockito.verify(observerData).onChanged(repositoryResponse.data)
        Mockito.verify(observerLoading, Mockito.times(4)).onChanged(false)
        Mockito.verify(observerMessage).onChanged("end of list")
    }
}
