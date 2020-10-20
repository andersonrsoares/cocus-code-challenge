package br.com.anderson.cocuscodechallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.PageCompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.mapper.CompletedChallengeMapper
import br.com.anderson.cocuscodechallenge.mapper.PageCompletedChallengeMapper
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
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
class CompletedChallengeRepositoryTest {

    private val codeWarsService = mock<CodeWarsService>()
    private val codeWarsDao = mock<CodeWarsDao>()
    private lateinit var completedChallengeRepository: CompletedChallengeRepository
    private val mapper = PageCompletedChallengeMapper(CompletedChallengeMapper())

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val db = mock<CodeWarsDb>()
        given(db.codeWarsDao()).willReturn(codeWarsDao)
        given(db.runInTransaction(ArgumentMatchers.any())).willCallRealMethod()

        completedChallengeRepository = CompletedChallengeRepository(codeWarsDao, codeWarsService, mapper)
    }

    @Test
    fun `test get completed challenges empty database`() {

        val username = "baz"

        given(codeWarsDao.allCompletedChallenges("baz")).willReturn(Single.just(arrayListOf()))
        val remoteData = PageCompletedChallengeDTO(
            totalPages = 1, totalItems = 1,
            data = arrayListOf(
                CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")
            )
        )

        given(codeWarsDao.insertCompletedChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getCompletedChallenges(username, 1)).willReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz", 1).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValues(DataSourceResult.create(mapper.map(remoteData)))
    }

    @Test
    fun `test get completed challenges `() {

        val username = "baz"

        val localData = PageCompletedChallenge(
            totalPages = 1, totalItems = 1,
            data = arrayListOf(
                CompletedChallenge(completedAt = 0, id = "id")
            )
        )

        given(codeWarsDao.allCompletedChallenges("baz")).willReturn(Single.just(localData.data))
        val remoteData = PageCompletedChallengeDTO(
            totalPages = 1, totalItems = 1,
            data = arrayListOf(
                CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")
            )
        )

        given(codeWarsDao.insertCompletedChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getCompletedChallenges(username, 1)).willReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz", 1).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(DataSourceResult.create(localData), DataSourceResult.create(mapper.map(remoteData)))
    }

    @Test
    fun `test get completed challenges page 2 dont call local data`() {

        val username = "baz"

        val remoteData = PageCompletedChallengeDTO(
            totalPages = 1, totalItems = 1,
            data = arrayListOf(
                CompletedChallengeDTO(completedAt = "2020-01-01T00:00:00Z", id = "id")
            )
        )

        given(codeWarsDao.insertCompletedChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getCompletedChallenges(username, 2)).willReturn(Single.just(remoteData))

        val testSubscriber = completedChallengeRepository.getCompletedChallenges("baz", 2).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(DataSourceResult.create(mapper.map(remoteData)))
    }

    @Test
    fun `test get completed challenges error remote database empty`() {
        val username = "baz"

        given(codeWarsDao.allCompletedChallenges("baz")).willReturn(Single.just(arrayListOf()))

        given(codeWarsDao.insertCompletedChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getCompletedChallenges(username, 1)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<CompletedChallengeDTO>>(500, "error".toResponseBody())
                )
            )
        )

        val testSubscriber = completedChallengeRepository.getCompletedChallenges(username, 1).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError
        }
    }
}
