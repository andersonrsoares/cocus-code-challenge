package br.com.anderson.cocuscodechallenge.repository



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.*
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
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
class AuthoredChallengeRepositoryTest {


    private val codeWarsService = Mockito.mock(CodeWarsService::class.java)
    private val codeWarsDao = Mockito.mock(CodeWarsDao::class.java)
    private lateinit var authoredChallengeRepository: AuthoredChallengeRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        val db = Mockito.mock(CodeWarsDb::class.java)
        Mockito.`when`(db.codeWarsDao()).thenReturn(codeWarsDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()

        authoredChallengeRepository = AuthoredChallengeRepository(codeWarsDao,codeWarsService)
    }



    @Test
    fun `test get authored challenges empty database`() {

        val username = "baz"

        Mockito.`when`(codeWarsDao.allAuthoredChallenges("baz")).thenReturn(Single.just(arrayListOf()))
        val remoteData = DataAuthoredChallengeDTO(data = arrayListOf(AuthoredChallengeDTO( id = "id" )))

        Mockito.`when`(codeWarsDao.insertAuthoredChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getAuthoredChallenges(username)).thenReturn(Single.just(remoteData))

        val testSubscriber = authoredChallengeRepository.getAuthoredChallenges("baz").test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValues(remoteData.toAuthoredChallengeList(username))


    }

    @Test
    fun `test get authored challenges `() {

        val username = "baz"

        val localData =  arrayListOf(AuthoredChallenge(id = "id"))

        Mockito.`when`(codeWarsDao.allAuthoredChallenges("baz")).thenReturn(Single.just(localData))
        val remoteData = DataAuthoredChallengeDTO(data = arrayListOf(AuthoredChallengeDTO(id = "id")))

        Mockito.`when`(codeWarsDao.insertAuthoredChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getAuthoredChallenges(username)).thenReturn(Single.just(remoteData))

        val testSubscriber = authoredChallengeRepository.getAuthoredChallenges("baz").test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(localData,
            remoteData.toAuthoredChallengeList(username))

    }




}