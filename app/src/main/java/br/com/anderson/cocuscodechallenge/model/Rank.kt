package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Rank(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = ""
)