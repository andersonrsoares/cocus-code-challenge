package br.com.anderson.cocuscodechallenge.dto

import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import com.google.gson.annotations.SerializedName


data class CompletedChallengeDTO(
    @SerializedName("completedAt")
    var completedAt: String? = null,
    @SerializedName("completedLanguages")
    var completedLanguages: List<String> = listOf(),
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("slug")
    var slug: String = ""
){
    fun toCompletedChallenge(): CompletedChallenge{
        return CompletedChallenge(completedAt = completedAt.toTimestamp(),
            name = name, completedLanguages = completedLanguages, id = id, slug = slug)
    }
}