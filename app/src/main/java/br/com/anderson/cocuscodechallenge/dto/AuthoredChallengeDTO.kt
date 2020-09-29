package br.com.anderson.cocuscodechallenge.dto

import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
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
){
    fun toAuthoredChallenge(): AuthoredChallenge{
        return AuthoredChallenge(rank = rank,id = id.toString(), name = name,description = description, tags = tags.orEmpty(), languages = languages.orEmpty() )
    }
}