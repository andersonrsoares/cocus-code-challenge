package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName


data class Languages(
    var language: HashMap<String,Language>? = null
)

open class Language(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("rank")
    var rank: Int = 0,
    @SerializedName("score")
    var score: Int = 0
)

class Overall : Language()