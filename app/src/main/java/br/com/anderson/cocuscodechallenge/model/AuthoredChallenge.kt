package br.com.anderson.cocuscodechallenge.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(value = [ListStringTypeConverter::class])
data class AuthoredChallenge(
    var description: String? = null,
    @PrimaryKey
    var id: String = "",
    var languages: List<String> = listOf(),
    var name: String? = null,
    var rank: Int? = null,
    var rankName: String? = null,
    var tags: List<String> = listOf(),
    var username: String? = null
)
