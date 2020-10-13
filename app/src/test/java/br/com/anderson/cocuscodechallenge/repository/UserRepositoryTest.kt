package br.com.anderson.cocuscodechallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.ApiUtil
import br.com.anderson.cocuscodechallenge.any
import br.com.anderson.cocuscodechallenge.dto.UserDTO
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
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class UserRepositoryTest {

    private val codeWarsService = Mockito.mock(CodeWarsService::class.java)
    private val codeWarsDao = Mockito.mock(CodeWarsDao::class.java)
    private lateinit var userRepository: UserRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val db = Mockito.mock(CodeWarsDb::class.java)
        Mockito.`when`(db.codeWarsDao()).thenReturn(codeWarsDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()

        userRepository = UserRepository(codeWarsDao, codeWarsService)
    }

    @Test
    fun `test get last users`() {

        val username = "baz"

        val response = listOf(User(datetime = 0, username = username))
        Mockito.`when`(codeWarsDao.allUsers()).thenReturn(Single.just(response))

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
    fun `test get user`() {
        val username = "baz"

        Mockito.`when`(codeWarsService.getUser(username)).thenReturn(Single.just(UserDTO(username = "foo")))
        Mockito.`when`(codeWarsDao.insertUser(any())).thenReturn(Completable.complete())

        val testSubscriber = userRepository.searchUser(username).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
    }

    @Test
    fun `test get user error remote 404`() {
        val username = "baz"

        Mockito.`when`(codeWarsService.getUser(username)).thenReturn(
            Single.error(
                HttpException(
                    Response.error<Single<UserDTO>>(404, ApiUtil.loadfile("not_found_response.json").toResponseBody())
                )
            )
        )

        Mockito.`when`(codeWarsDao.insertUser(any())).thenReturn(Completable.complete())

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

        Mockito.`when`(codeWarsService.getUser(username)).thenReturn(
            Single.error(
                HttpException(
                    Response.error<Single<UserDTO>>(500, "error".toResponseBody())
                )
            )
        )

        Mockito.`when`(codeWarsDao.insertUser(any())).thenReturn(Completable.complete())

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
