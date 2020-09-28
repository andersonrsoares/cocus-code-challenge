package br.com.anderson.cocuscodechallenge.dto

import com.google.gson.annotations.SerializedName

data class PageCompletedChallengeDTO(
    @SerializedName("data")
    var data: List<CompletedChallengeDTO>? = null,
    @SerializedName("totalItems")
    var totalItems: Int? = null,
    @SerializedName("totalPages")
    var totalPages: Int? = null
):BaseDTO()