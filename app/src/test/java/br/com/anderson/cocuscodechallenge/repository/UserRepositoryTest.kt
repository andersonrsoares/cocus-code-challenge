package br.com.anderson.cocuscodechallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.ApiUtil
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.UserDTO
import br.com.anderson.cocuscodechallenge.mock
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.model.User
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
class UserRepositoryTest {

    private val codeWarsService = mock<CodeWarsService>()
    private val codeWarsDao = mock<CodeWarsDao>()
    private lateinit var userRepository: UserRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val db = mock<CodeWarsDb>()
        given(db.codeWarsDao()).willReturn(codeWarsDao)
        given(db.runInTransaction(ArgumentMatchers.any())).willCallRealMethod()

        userRepository = UserRepository(codeWarsDao, codeWarsService)
    }

    @Test
    fun `test get last users`() {

        val username = "baz"

        val response = listOf(User(datetime = 0, username = username))
        given(codeWarsDao.allUsers()).willReturn(Single.just(response))

        val testSubscriber = userRepository.listLastUsers().test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it == DataSourceResult.create(response)
        }
    }


    @Test
    fun `test order users by rank`() {

        val response = listOf(User(datetime = 0, leaderboardPosition = 2, username = "baz"),User(datetime = 1, leaderboardPosition = 1, username = "foo"))
        given(codeWarsDao.allUsers()).willReturn(Single.just(response))

        val testSubscriber = userRepository.listOrderByPosition().test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.body?.firstOrNull()?.leaderboardPosition == 1
        }
    }

    @Test
    fun `test order users by lookup`() {

        val response = listOf(User(datetime = 1, leaderboardPosition = 2, username = "baz"),User(datetime = 0, leaderboardPosition = 1, username = "foo"))
        given(codeWarsDao.allUsers()).willReturn(Single.just(response))

        val testSubscriber = userRepository.listOrderByLookUp().test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.body?.firstOrNull()?.datetime == 1L
        }
    }

    @Test
    fun `test get user`() {
        val username = "baz"

        given(codeWarsService.getUser(username)).willReturn(Single.just(UserDTO(username = "foo")))
        given(codeWarsDao.insertUser(any())).willReturn(Completable.complete())

        val testSubscriber = userRepository.searchUser(username).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
    }

    @Test
    fun `test get user error remote 404`() {
        val username = "baz"

        given(codeWarsService.getUser(username)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<UserDTO>>(404, ApiUtil.loadfile("not_found_response.json").toResponseBody())
                )
            )
        )

        given(codeWarsDao.insertUser(any())).willReturn(Completable.complete())

        val testSubscriber = userRepository.searchUser(username).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.NotFound
        }
    }

    @Test
    fun `test get user error remote 500`() {
        val username = "baz"

        given(codeWarsService.getUser(username)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<UserDTO>>(500, "error".toResponseBody())
                )
            )
        )

        given(codeWarsDao.insertUser(any())).willReturn(Completable.complete())

        val testSubscriber = userRepository.searchUser(username).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError
        }
    }
}
