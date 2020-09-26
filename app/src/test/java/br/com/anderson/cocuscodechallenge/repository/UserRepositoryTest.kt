package br.com.anderson.cocuscodechallenge.repository



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
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
    fun setup(){
        val db = Mockito.mock(CodeWarsDb::class.java)
        Mockito.`when`(db.codeWarsDao()).thenReturn(codeWarsDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()

        userRepository = UserRepository(codeWarsDao,codeWarsService)
    }



    @Test
    fun `test get last users`() {

        val username = "baz"

        val response = listOf(User(datetime = 0,username = username))
        Mockito.`when`(codeWarsDao.allUsers()).thenReturn(Flowable.just(response))

        val testSubscriber = TestSubscriber<List<User>>()

        userRepository.listLastUsers().subscribe(testSubscriber)

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it == response
        }

    }

    /*@Test
    fun `test get all users`() {

        val username = "baz"
        val maxResults = 3

        Mockito.`when`(codeWarsService.getUser(username)).thenReturn(Single.just(UserDTO(username = "foo")))


        userRepository.searchUser(username).test().assertValue {
            it.
        }

        val success = listOf(GeocodeAddress("foo", Geometry(LocationGeocode())))

        Mockito.verify(observer).onChanged(Resource.success(success))
    }*/


}