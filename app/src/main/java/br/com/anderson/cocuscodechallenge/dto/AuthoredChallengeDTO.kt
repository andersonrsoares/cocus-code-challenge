package br.com.anderson.cocuscodechallenge.dto

import com.google.gson.annotations.SerializedName

data class AuthoredChallengeDTO(
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("languages")
    val languages: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("rank")
    val rank: Int? = null,
    @SerializedName("rankName")
    val rankName: String? = null,
    @SerializedName("tags")
    val tags: List<String>? = null
)
