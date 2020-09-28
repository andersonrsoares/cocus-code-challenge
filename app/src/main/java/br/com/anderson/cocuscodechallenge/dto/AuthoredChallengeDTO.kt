package br.com.anderson.cocuscodechallenge.dto

import com.google.gson.annotations.SerializedName

data class AuthoredChallengeDTO(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("languages")
    var languages: List<String> = listOf(),
    @SerializedName("name")
    var name: String = "",
    @SerializedName("rank")
    var rank: Int = 0,
    @SerializedName("rankName")
    var rankName: String = "",
    @SerializedName("tags")
    var tags: List<String> = listOf()
)