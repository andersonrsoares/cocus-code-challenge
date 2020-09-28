package br.com.anderson.cocuscodechallenge.dto

import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import com.google.gson.annotations.SerializedName

data class PageCompletedChallengeDTO(
    @SerializedName("data")
    var data: List<CompletedChallengeDTO>? = null,
    @SerializedName("totalItems")
    var totalItems: Int? = null,
    @SerializedName("totalPages")
    var totalPages: Int? = null
):BaseDTO(){
    fun toPageCompletedChallenge(username:String): PageCompletedChallenge {
        return PageCompletedChallenge(totalItems = totalItems, data = data?.map { it.toCompletedChallenge().apply { this.username = username } }, totalPages = totalPages )
    }
}