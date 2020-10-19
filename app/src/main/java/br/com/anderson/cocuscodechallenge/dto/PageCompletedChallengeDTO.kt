package br.com.anderson.cocuscodechallenge.dto

import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import com.google.gson.annotations.SerializedName

data class PageCompletedChallengeDTO(
    @SerializedName("data")
    val data: List<CompletedChallengeDTO>? = null,
    @SerializedName("totalItems")
    val totalItems: Int? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null
) : BaseDTO()
