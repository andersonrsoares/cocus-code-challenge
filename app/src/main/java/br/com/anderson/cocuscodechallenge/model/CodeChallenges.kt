package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class CodeChallenges(
    @SerializedName("totalAuthored")
    val totalAuthored: Int = 0,
    @SerializedName("totalCompleted")
    val totalCompleted: Int = 0
)
