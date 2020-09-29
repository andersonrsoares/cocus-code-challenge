package br.com.anderson.cocuscodechallenge.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(value = [ListStringTypeConverter::class])
data class AuthoredChallenge(
    @SerializedName("description")
    var description: String? =  null,
    @SerializedName("id")
    @PrimaryKey
    var id: String = "",
    @SerializedName("languages")
    var languages: List<String> = listOf(),
    @SerializedName("name")
    var name: String? =  null,
    @SerializedName("rank")
    var rank: Int? = null,
    @SerializedName("rankName")
    var rankName: String? =  null,
    @SerializedName("tags")
    var tags: List<String> = listOf(),
    @SerializedName("username")
    var username: String? = null
)