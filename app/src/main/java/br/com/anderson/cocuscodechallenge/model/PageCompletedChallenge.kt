package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class PageCompletedChallenge(
    @SerializedName("data")
    var data: List<CompletedChallenge>? = null,
    @SerializedName("totalItems")
    var totalItems: Int? = null,
    @SerializedName("totalPages")
    var totalPages: Int? = null
)