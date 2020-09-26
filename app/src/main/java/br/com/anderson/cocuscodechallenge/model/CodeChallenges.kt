package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class CodeChallenges(
    @SerializedName("totalAuthored")
    var totalAuthored: Int = 0,
    @SerializedName("totalCompleted")
    var totalCompleted: Int = 0
)
