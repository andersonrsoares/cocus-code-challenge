package br.com.anderson.cocuscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Ranks(
    @SerializedName("languages")
    val languages: Languages? = null,
    @SerializedName("overall")
    val overall: Overall? = null
)
