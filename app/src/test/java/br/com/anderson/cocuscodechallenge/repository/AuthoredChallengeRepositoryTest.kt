package br.com.anderson.cocuscodechallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.AuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.DataAuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class AuthoredChallengeRepositoryTest {

    private val codeWarsService = mock<CodeWarsService>()
    private val codeWarsDao = mock<CodeWarsDao>()
    private lateinit var authoredChallengeRepository: AuthoredChallengeRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val db = mock<CodeWarsDb>()
        given(db.codeWarsDao()).willReturn(codeWarsDao)
        given(db.runInTransaction(ArgumentMatchers.any())).willCallRealMethod()

        authoredChallengeRepository = AuthoredChallengeRepository(codeWarsDao, codeWarsService)
    }

    @Test
    fun `test get authored challenges empty database`() {

        val username = "baz"

        given(codeWarsDao.allAuthoredChallenges("baz")).willReturn(Single.just(arrayListOf()))
        val remoteData = DataAuthoredChallengeDTO(data = arrayListOf(AuthoredChallengeDTO(id = "id")))

        given(codeWarsDao.insertAuthoredChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getAuthoredChallenges(username)).willReturn(Single.just(remoteData))

        val testSubscriber = authoredChallengeRepository.getAuthoredChallenges("baz").test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValues(DataSourceResult.create(remoteData.toAuthoredChallengeList(username)))
    }

    @Test
    fun `test get authored challenges `() {

        val username = "baz"

        val localData = arrayListOf(AuthoredChallenge(id = "id"))

        given(codeWarsDao.allAuthoredChallenges("baz")).willReturn(Single.just(localData))
        val remoteData = DataAuthoredChallengeDTO(data = arrayListOf(AuthoredChallengeDTO(id = "id")))

        given(codeWarsDao.insertAuthoredChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getAuthoredChallenges(username)).willReturn(Single.just(remoteData))

        val testSubscriber = authoredChallengeRepository.getAuthoredChallenges("baz").test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(
            DataSourceResult.create(localData),
            DataSourceResult.create(remoteData.toAuthoredChallengeList(username))
        )
    }

    @Test
    fun `test get authored challenges error remote database empty`() {
        val username = "baz"

        given(codeWarsDao.allAuthoredChallenges("baz")).willReturn(Single.just(arrayListOf()))

        given(codeWarsDao.insertAuthoredChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getAuthoredChallenges(username)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<CompletedChallengeDTO>>(500, "error".toResponseBody())
                )
            )
        )

        val testSubscriber = authoredChallengeRepository.getAuthoredChallenges(username).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError
        }
    }
}
