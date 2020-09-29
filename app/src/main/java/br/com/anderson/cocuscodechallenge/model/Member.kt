package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("url")
    val url: String = "",
    @SerializedName("username")
    val username: String = ""
)
