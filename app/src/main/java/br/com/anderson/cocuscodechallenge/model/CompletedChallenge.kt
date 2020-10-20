package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter

@Entity
@TypeConverters(value = [ListStringTypeConverter::class])
data class CompletedChallenge(
    val completedAt: Long? = null,
    val completedLanguages: List<String>? = null,
    @PrimaryKey
    val id: String = "",
    val name: String? = null,
    val slug: String? = null,
    var username: String? = null
)
