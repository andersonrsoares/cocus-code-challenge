package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import com.google.gson.annotations.SerializedName


@Entity
@TypeConverters(value = [ListStringTypeConverter::class])
data class CompletedChallenge(
    @SerializedName("completedAt")
    val completedAt: Long? = null,
    @SerializedName("completedLanguages")
    val completedLanguages: List<String>? = null,
    @SerializedName("id")
    @PrimaryKey
    val id: String = "",
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("slug")
    val slug: String? = null,
    @SerializedName("username")
    var username: String? = null
)