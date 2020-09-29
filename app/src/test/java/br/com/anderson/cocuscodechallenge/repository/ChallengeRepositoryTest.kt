package br.com.anderson.cocuscodechallenge.repository



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.*
import br.com.anderson.cocuscodechallenge.model.*
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
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
class ChallengeRepositoryTest {


    private val codeWarsService = Mockito.mock(CodeWarsService::class.java)
    private val codeWarsDao = Mockito.mock(CodeWarsDao::class.java)
    private lateinit var challengeRepository: ChallengeRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        val db = Mockito.mock(CodeWarsDb::class.java)
        Mockito.`when`(db.codeWarsDao()).thenReturn(codeWarsDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()

        challengeRepository = ChallengeRepository(codeWarsDao,codeWarsService)
    }

    @Test
    fun `test get challenge empty database`() {

        val id = "id"

        Mockito.`when`(codeWarsDao.getChallenge(id)).thenReturn(Maybe.empty())
        val remoteData = ChallengeDTO(id = id )

        Mockito.`when`(codeWarsDao.insertChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getChallenge(id)).thenReturn(Single.just(remoteData))

        val testSubscriber = challengeRepository.getAuthoredChallenges(id).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(remoteData.toChallange())


    }

    @Test
    fun `test get challenge `() {

        val id = "id"

        val localData =  Challenge(id = id)

        Mockito.`when`(codeWarsDao.getChallenge(id)).thenReturn(Maybe.just(localData))
        val remoteData = ChallengeDTO(id = id)

        Mockito.`when`(codeWarsDao.insertChallenge(any())).thenReturn(Completable.complete())
        Mockito.`when`(codeWarsService.getChallenge(id)).thenReturn(Single.just(remoteData))

        val testSubscriber = challengeRepository.getAuthoredChallenges(id).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(localData,
            remoteData.toChallange())

    }
}