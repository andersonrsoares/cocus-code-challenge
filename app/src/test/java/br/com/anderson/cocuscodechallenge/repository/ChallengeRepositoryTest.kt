package br.com.anderson.cocuscodechallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.ChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.mapper.ChallengeMapper
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Completable
import io.reactivex.Maybe
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
class ChallengeRepositoryTest {

    private val codeWarsService = mock<CodeWarsService>()
    private val codeWarsDao = mock<CodeWarsDao>()
    private lateinit var challengeRepository: ChallengeRepository
    private val mapper = ChallengeMapper()

    @Before
    fun setup() {
        val db = mock<CodeWarsDb>()
        given(db.codeWarsDao()).willReturn(codeWarsDao)
        given(db.runInTransaction(ArgumentMatchers.any())).willCallRealMethod()

        challengeRepository = ChallengeRepository(codeWarsDao, codeWarsService, mapper)
    }

    @Test
    fun `test get challenge empty database`() {

        val id = "id"

        given(codeWarsDao.getChallenge(id)).willReturn(Maybe.empty())
        val remoteData = ChallengeDTO(id = id)

        given(codeWarsDao.insertChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getChallenge(id)).willReturn(Single.just(remoteData))

        val testSubscriber = challengeRepository.getChallenge(id).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(DataSourceResult.create(mapper.map(remoteData)))
    }

    @Test
    fun `test get challenge `() {

        val id = "id"

        val localData = Challenge(id = id)

        given(codeWarsDao.getChallenge(id)).willReturn(Maybe.just(localData))
        val remoteData = ChallengeDTO(id = id)

        given(codeWarsDao.insertChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getChallenge(id)).willReturn(Single.just(remoteData))

        val testSubscriber = challengeRepository.getChallenge(id).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValues(
            DataSourceResult.create(localData),
            DataSourceResult.create(mapper.map(remoteData))
        )
    }

    @Test
    fun `test get challenge remote error database empty`() {

        val id = "id"

        given(codeWarsDao.getChallenge(id)).willReturn(Maybe.empty())

        given(codeWarsDao.insertChallenge(any())).willReturn(Completable.complete())
        given(codeWarsService.getChallenge(id)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<CompletedChallengeDTO>>(500, "error".toResponseBody())
                )
            )
        )

        val testSubscriber = challengeRepository.getChallenge(id).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError
        }
    }
}
