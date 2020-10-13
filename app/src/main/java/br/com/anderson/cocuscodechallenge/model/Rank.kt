package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Rank(
    @SerializedName("color")
    val color: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = ""
)
