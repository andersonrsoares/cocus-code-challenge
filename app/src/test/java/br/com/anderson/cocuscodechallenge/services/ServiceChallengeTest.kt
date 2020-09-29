package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.ApiUtil
import org.junit.*
import com.google.common.truth.Truth.assertThat
import com.google.gson.stream.MalformedJsonException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class ServiceChallengeTest : BaseServiceTest() {

    @Test
    fun `test challenge response success full data`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"challenge.json")

        val response = service.getChallenge("id").test()

        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/code-challenges/id")
        //THEN

        response.assertNoErrors()
        val dataResponse =  response.values().first()

        assertThat(dataResponse).isNotNull()
        assertThat(dataResponse.languages).hasSize(2)
        assertThat(dataResponse.tags).hasSize(4)
        assertThat(dataResponse.approvedAt).isEqualTo("2013-12-20T14:53:06Z")
        assertThat(dataResponse.publishedAt).isEqualTo("2013-11-05T00:07:31Z")
        assertThat(dataResponse.name).isEqualTo("Valid Braces")
        assertThat(dataResponse.slug).isEqualTo("valid-braces")
        assertThat(dataResponse.id).isEqualTo("5277c8a221e209d3f6000b56")
        assertThat(dataResponse.category).isEqualTo("algorithms")
        assertThat(dataResponse.url).isEqualTo("http://www.codewars.com/kata/valid-braces")
        assertThat(dataResponse.rank?.id).isEqualTo(-4)
        assertThat(dataResponse.rank?.name).isEqualTo("4 kyu")
        assertThat(dataResponse.rank?.color).isEqualTo("blue")
        assertThat(dataResponse.createdBy?.username).isEqualTo("xDranik")
        assertThat(dataResponse.createdBy?.url).isEqualTo("http://www.codewars.com/users/xDranik")
        assertThat(dataResponse.approvedBy?.username).isEqualTo("xDranik")
        assertThat(dataResponse.approvedBy?.url).isEqualTo("http://www.codewars.com/users/xDranik")
        assertThat(dataResponse.description).contains("Write a function called")
        assertThat(dataResponse.totalAttempts).isEqualTo(4911)
        assertThat(dataResponse.totalCompleted).isEqualTo(919)
        assertThat(dataResponse.totalStars).isEqualTo(12)
    }

    @Test
    fun `test completed challenges  response not json`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"error_json_response.html")

        val response = service.getChallenge("id").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/code-challenges/id")
        //THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test completed challenges not found`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer =  mockWebServer,fileName = "not_found_response.json",statuscode = 404)

        val response = service.getChallenge("id").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/code-challenges/id")

        //THEN
        response.assertError {
            it is HttpException && (it as? HttpException)?.code() == 404
        }
    }

}