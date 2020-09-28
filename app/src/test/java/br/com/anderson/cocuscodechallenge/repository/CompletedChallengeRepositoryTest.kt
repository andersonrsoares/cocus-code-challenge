package br.com.anderson.cocuscodechallenge.repository



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.PageCompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.UserDTO
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.concurrent.TimeUnit


@RunWith(JUnit4::class)
class CompletedChallengeRepositoryTest {


    private val codeWarsService = Mockito.mock(CodeWarsService::class.java)
    private val codeWarsDao = Mockito.mock(CodeWarsDao::class.java)
    private lateinit var completedChallengeRepository: CompletedChallengeRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        val db = Mockito.mock(CodeWarsDb::class.java)
        Mockito.`when`(db.codeWarsDao()).thenReturn(codeWarsDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()

        completedChallengeRepository = CompletedChallengeRepository(codeWarsDao,codeWarsService)
    }



    @Test
    fun `test get completed challenges empty database`() {

        val username = "baz"

        Mockito.`when`(codeWarsDao.allCompletedChallenges("baz")).thenReturn(Single.just(arrayListOf()))
        val remoteData = PageCompletedChallengeDTO(totalPages = 1,totalItems = 1,data = arrayListOf(
            CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")))

        Mockito.`when`(codeWarsDao.insertCompletedChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getCompletedChallenges(username,1)).thenReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz",1).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(PageCompletedChallenge(totalPages = 1,totalItems = 0,data = arrayListOf()),
            remoteData.toPageCompletedChallenge(username))


    }

    @Test
    fun `test get completed challenges `() {

        val username = "baz"

        val localData = PageCompletedChallenge(totalPages = 1,totalItems = 1,data = arrayListOf(
            CompletedChallenge(completedAt = 0, id = "id")))

        Mockito.`when`(codeWarsDao.allCompletedChallenges("baz")).thenReturn(Single.just(localData.data))
        val remoteData = PageCompletedChallengeDTO(totalPages = 1,totalItems = 1,data = arrayListOf(
            CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")))

        Mockito.`when`(codeWarsDao.insertCompletedChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getCompletedChallenges(username,1)).thenReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz",1).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(localData,
            remoteData.toPageCompletedChallenge(username))

    }




}