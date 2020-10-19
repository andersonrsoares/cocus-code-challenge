package br.com.anderson.cocuscodechallenge.dto

import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import com.google.gson.annotations.SerializedName

data class CompletedChallengeDTO(
    @SerializedName("completedAt")
    var completedAt: String? = null,
    @SerializedName("completedLanguages")
    val completedLanguages: List<String> = listOf(),
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("slug")
    val slug: String = ""
)