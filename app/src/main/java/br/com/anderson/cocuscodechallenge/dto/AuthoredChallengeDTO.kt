package br.com.anderson.cocuscodechallenge.dto

import com.google.gson.annotations.SerializedName

data class AuthoredChallengeDTO(
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("languages")
    var languages: List<String>? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("rank")
    var rank: Int? = null,
    @SerializedName("rankName")
    var rankName: String? = null,
    @SerializedName("tags")
    var tags: List<String>?= null
)