package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.ApiUtil
import org.junit.*
import com.google.common.truth.Truth.assertThat
import com.google.gson.stream.MalformedJsonException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class ServiceAuthoredChallengeTest : BaseServiceTest() {

    @Test
    fun `test completed challenges response success full data`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"authored_challenges.json")

        val response = service.getAuthoredChallenges("username").test()

        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/authored")
        //THEN

        response.assertNoErrors()
        val dataResponse =  response.values().first()

        assertThat(dataResponse.data).isNotNull()
        assertThat(dataResponse.data).hasSize(2)
        assertThat(dataResponse.data?.get(0)?.description).contains("For this kata you will")
        assertThat(dataResponse.data?.get(0)?.rankName).isEqualTo("3 kyu")
        assertThat(dataResponse.data?.get(0)?.id).isEqualTo("5571d9fc11526780a000011a")
        assertThat(dataResponse.data?.get(0)?.name).isEqualTo("The builder of things")
        assertThat(dataResponse.data?.get(0)?.tags).contains("Algorithms")
        assertThat(dataResponse.data?.get(0)?.languages).contains("ruby")
    }

    @Test
    fun `test completed challenges esponse not json`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"error_json_response.html")

        val response = service.getAuthoredChallenges("username").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/authored")
        //THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test completed challenges not found`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer =  mockWebServer,fileName = "not_found_response.json",statuscode = 404)

        val response = service.getAuthoredChallenges("username").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/authored")

        //THEN
        response.assertError {
            it is HttpException && (it as? HttpException)?.code() == 404
        }
    }

}