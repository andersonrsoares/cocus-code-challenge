package br.com.anderson.cocuscodechallenge.dto

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
