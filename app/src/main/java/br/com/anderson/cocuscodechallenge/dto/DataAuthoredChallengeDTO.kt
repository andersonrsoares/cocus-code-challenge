package br.com.anderson.cocuscodechallenge.dto
import com.google.gson.annotations.SerializedName

data class DataAuthoredChallengeDTO(
    @SerializedName("data")
    var data: List<AuthoredChallengeDTO>? = null
):BaseDTO()

