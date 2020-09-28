package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.dto.PageCompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.UserDTO
import io.reactivex.Single

import retrofit2.http.*

/**
 * REST API access points
 */


interface  CodeWarsService {

    @GET("users/{username}")
    fun getUser(@Path("username") username:String ): Single<UserDTO>

    @GET("{username}/code-challenges/completed")
    fun getCompletedChallenges(@Path("username") username:String,@Query("page") page:Int = 0): Single<PageCompletedChallengeDTO>

}
