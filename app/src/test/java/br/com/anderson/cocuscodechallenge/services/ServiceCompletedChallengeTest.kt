package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.ApiUtil
import org.junit.*
import com.google.common.truth.Truth.assertThat
import com.google.gson.stream.MalformedJsonException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class ServiceCompletedChallengeTest : BaseServiceTest() {

    @Test
    fun `test completed challages response success full data`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"completed_challages.json")

        val response = service.getCompletedChallenges("username").test()

        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/completed?page=0")
        //THEN

        response.assertNoErrors()
        val dataResponse =  response.values().first()


        assertThat(dataResponse.totalItems).isEqualTo(1)
        assertThat(dataResponse.totalPages).isEqualTo(1)
        assertThat(dataResponse.data).isNotNull()
        assertThat(dataResponse.data).hasSize(1)
        assertThat(dataResponse.data?.get(0)?.completedAt).isEqualTo("2017-04-06T16:32:09Z")
        assertThat(dataResponse.data?.get(0)?.completedLanguages).isNotEmpty()
        assertThat(dataResponse.data?.get(0)?.completedLanguages).hasSize(11)
        assertThat(dataResponse.data?.get(0)?.id).isEqualTo("514b92a657cdc65150000006")
        assertThat(dataResponse.data?.get(0)?.name).isEqualTo("Multiples of 3 and 5")
        assertThat(dataResponse.data?.get(0)?.slug).isEqualTo("multiples-of-3-and-5")

    }

    @Test
    fun `test completed challages response success page end`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"completed_challages_page_end.json")

        val response = service.getCompletedChallenges("username",1).test()

        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/completed?page=1")
        //THEN

        response.assertNoErrors()
        val dataResponse =  response.values().first()


        assertThat(dataResponse.totalItems).isEqualTo(1)
        assertThat(dataResponse.totalPages).isEqualTo(1)
        assertThat(dataResponse.data).isEmpty()

    }

    @Test
    fun `test completed challages  response not json`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer,"error_json_response.html")

        val response = service.getCompletedChallenges("username").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/completed?page=0")
        //THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test completed challages not found`() {
        //GIVEN
        ApiUtil.enqueueResponse(mockWebServer =  mockWebServer,fileName = "not_found_response.json",statuscode = 404)

        val response = service.getCompletedChallenges("username").test()
        //when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/users/username/code-challenges/completed?page=0")

        //THEN
        response.assertError {
            it is HttpException && (it as? HttpException)?.code() == 404
        }
    }

}