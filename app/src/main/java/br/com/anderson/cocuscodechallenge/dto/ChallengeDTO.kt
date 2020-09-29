package br.com.anderson.cocuscodechallenge.dto
import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.Member
import br.com.anderson.cocuscodechallenge.model.Rank
import com.google.gson.annotations.SerializedName


data class ChallengeDTO(
    @SerializedName("approvedAt")
    val approvedAt: String? = null,
    @SerializedName("approvedBy")
    val approvedBy: Member? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("createdBy")
    val createdBy: Member? = null,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("languages")
    val languages: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
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
):BaseDTO(){
    fun toChallange(): Challenge {
        return Challenge(publishedAt = publishedAt?.toTimestamp(),
            languages = languages, tags = tags, description =  description,
            id = id.toString(), rank = rank, slug = slug, name = name,
            approvedAt = approvedAt?.toTimestamp(), approvedBy = approvedBy, category = category,
            createdBy = createdBy, totalAttempts = totalAttempts, totalCompleted = totalCompleted,
            totalStars = totalStars, url = url)
    }
}





