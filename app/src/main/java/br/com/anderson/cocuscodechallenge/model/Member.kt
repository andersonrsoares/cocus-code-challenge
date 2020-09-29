package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("url")
    var url: String = "",
    @SerializedName("username")
    var username: String = ""
)
