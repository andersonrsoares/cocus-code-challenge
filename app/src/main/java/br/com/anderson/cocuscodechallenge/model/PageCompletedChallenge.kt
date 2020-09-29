package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class PageCompletedChallenge(
    @SerializedName("data")
    val data: List<CompletedChallenge>? = null,
    @SerializedName("totalItems")
    val totalItems: Int? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null
)