package br.com.anderson.cocuscodechallenge.dto

import com.google.gson.annotations.SerializedName

open class BaseDTO(
    @SerializedName("reason")
    val reason: String? = null,
    @SerializedName("success")
    val success: Boolean? = null
)