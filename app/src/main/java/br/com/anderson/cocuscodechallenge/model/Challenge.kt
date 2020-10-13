package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.MemberTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.RankTypeConverter

@Entity
@TypeConverters(value = [ListStringTypeConverter::class, RankTypeConverter::class, MemberTypeConverter::class])
data class Challenge(
    val approvedAt: Long? = null,
    val approvedBy: Member? = null,
    val category: String? = null,
    val createdBy: Member? = null,
    val description: String = "",
    @PrimaryKey
    val id: String = "",
    val languages: List<String>? = null,
    val name: String? = null,
    val publishedAt: Long? = null,
    val rank: Rank? = null,
    val slug: String? = null,
    val tags: List<String>? = null,
    val totalAttempts: Int? = null,
    val totalCompleted: Int? = null,
    val totalStars: Int? = null,
    val url: String = ""
)
