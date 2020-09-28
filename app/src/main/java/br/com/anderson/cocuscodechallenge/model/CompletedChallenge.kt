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
    var completedAt: Long? = null,
    @SerializedName("completedLanguages")
    var completedLanguages: List<String>? = null,
    @SerializedName("id")
    @PrimaryKey
    var id: String = "",
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("slug")
    var slug: String? = null
)