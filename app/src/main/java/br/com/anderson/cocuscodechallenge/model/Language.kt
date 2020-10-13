package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Languages(
    var language: List<Language>? = null
)

open class Language(
    var languageName: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("score")
    val score: Int = 0
)

class Overall : Language()
