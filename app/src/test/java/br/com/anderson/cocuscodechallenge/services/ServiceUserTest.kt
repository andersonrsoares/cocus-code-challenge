package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.ApiUtil
import com.google.common.truth.Truth.assertThat
import com.google.gson.stream.MalformedJsonException
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class ServiceUserTest : BaseServiceTest() {

    @Test
    fun `test user response success full data`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer, "user.json")

        val response = service.getUser("username").test()

        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username")
        // THEN

        response.assertNoErrors()
        val user = response.values().first()
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
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer, "error_json_response.html")

        val response = service.getUser("username").test()
        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username")
        // THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test user not found`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer = mockWebServer, fileName = "not_found_response.json", statuscode = 404)

        val response = service.getUser("notfound").test()
        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/notfound")

        // THEN
        response.assertError {
            // print((it as? HttpException)?.response()?.errorBody()?.string())
            it is HttpException && (it as? HttpException)?.code() == 404
        }
    }
}
