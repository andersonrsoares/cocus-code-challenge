
package br.com.anderson.cocuscodechallenge.services

import br.com.anderson.cocuscodechallenge.dto.UserDTO
import io.reactivex.Single

import retrofit2.http.*

/**
 * REST API access points
 */


interface  CodeWarsService {

    @GET("users/{username}")
    fun getUser(@Path("username") username:String ): Single<UserDTO>

}
