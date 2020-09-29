package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.MemberTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.RankTypeConverter
import com.google.gson.annotations.SerializedName


@Entity
@TypeConverters(value = [ListStringTypeConverter::class,RankTypeConverter::class,MemberTypeConverter::class])
data class Challenge(
    @SerializedName("approvedAt")
    val approvedAt: Long? = null,
    @SerializedName("approvedBy")
    val approvedBy: Member? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("createdBy")
    val createdBy: Member? = null,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    @PrimaryKey
    val id: String = "",
    @SerializedName("languages")
    val languages: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: Long? = null,
    @SerializedName("rank")
    val rank: Rank? = null,
    @SerializedName("slug")
    val slug: String? = null,
    @SerializedName("tags")
    val tags: List<String>? = null,
    @SerializedName("totalAttempts")
    val totalAttempts: Int? = null,
    @SerializedName("totalCompleted")
    val totalCompleted: Int? = null,
    @SerializedName("totalStars")
    val totalStars: Int? = null,
    @SerializedName("url")
    val url: String = ""
)





