package br.com.anderson.cocuscodechallenge.repository



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.PageCompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.UserDTO
import br.com.anderson.cocuscodechallenge.model.*
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
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
        testSubscriber.assertNotComplete()
        testSubscriber.assertValues( DataSourceResult.create(remoteData.toPageCompletedChallenge(username)))


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
        testSubscriber.assertValues( DataSourceResult.create(localData),  DataSourceResult.create(remoteData.toPageCompletedChallenge(username)))

    }


    @Test
    fun `test get completed challenges page 2 dont call local data`() {

        val username = "baz"

        val remoteData = PageCompletedChallengeDTO(totalPages = 1,totalItems = 1,data = arrayListOf(
            CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")))

        Mockito.`when`(codeWarsDao.insertCompletedChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getCompletedChallenges(username,2)).thenReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz",2).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues( DataSourceResult.create(remoteData.toPageCompletedChallenge(username)))

    }


    @Test
    fun `test get completed challenges error remote database empty`() {
        val username = "baz"


        Mockito.`when`(codeWarsDao.allCompletedChallenges("baz")).thenReturn(Single.just(arrayListOf()))


        Mockito.`when`(codeWarsDao.insertCompletedChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getCompletedChallenges(username,1)).thenReturn(Single.error(
            HttpException(
                Response.error<Single<CompletedChallengeDTO>>(500, "error".toResponseBody()))
        ))

        val testSubscriber =  completedChallengeRepository.getCompletedChallenges(username,1).test()


        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError
        }
    }

}