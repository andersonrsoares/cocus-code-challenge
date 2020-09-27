package br.com.anderson.cocuscodechallenge.services


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.ApiUtil
import br.com.anderson.cocuscodechallenge.extras.LanguageDeserializer
import br.com.anderson.cocuscodechallenge.model.Languages
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.google.gson.stream.MalformedJsonException
import retrofit2.HttpException


@RunWith(JUnit4::class)
class CodeWarsServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: CodeWarsService

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        val gson = GsonBuilder().apply {
            registerTypeAdapter(Languages::class.java, LanguageDeserializer())
        }.create()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CodeWarsService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test user response success full data`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"user.json")

        val response = service.getUser("username").test()

        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username")
        //THEN

        response.assertNoErrors()
        val user =  response.values().first()
        assertThat(user.name).isEqualTo("Some Person")
        assertThat(user.username).isEqualTo("some_user")
        assertThat(user.honor).isEqualTo(544)
        assertThat(user.clan).isEqualTo("some clan")
        assertThat(user.leaderboardPosition).isEqualTo(134)
        assertThat(user.skills).isNotEmpty()
        assertThat(user.ranks).isNotNull()
        assertThat(user.ranks?.overall).isNotNull()
        assertThat(user.ranks?.languages).isNotNull()
        assertThat(user.ranks?.languages?.language).isNotNull()
        assertThat(user.ranks?.languages?.language).hasSize(3)
        assertThat(user.ranks?.languages?.language?.get(2)?.color).isEqualTo("blue")
        assertThat(user.ranks?.languages?.language?.get(2)?.rank).isEqualTo(-4)
        assertThat(user.ranks?.languages?.language?.get(2)?.name).isEqualTo("4 kyu")
        assertThat(user.ranks?.languages?.language?.get(2)?.score).isEqualTo(870)

        assertThat(user.codeChallenges).isNotNull()
        assertThat(user.codeChallenges?.totalAuthored).isEqualTo(3)
        assertThat(user.codeChallenges?.totalCompleted).isEqualTo(230)
    }


    @Test
    fun `test user response not json`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"user_not_json_response.html")

        val response = service.getUser("username").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username")
        //THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test user not found`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer =  mockWebServer,fileName = "user_not_found.json",statuscode = 404)

        val response = service.getUser("notfound").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/notfound")

        //THEN
        response.assertError {
            it is HttpException && (it as? HttpException)?.code() == 404
        }
    }



}