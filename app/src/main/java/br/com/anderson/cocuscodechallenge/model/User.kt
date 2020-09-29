package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.CodeChallengesTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.RanksTypeConverter


@Entity
@TypeConverters(value = [CodeChallengesTypeConverter::class,RanksTypeConverter::class,ListStringTypeConverter::class])
data class User(
    val clan: String? = null,
    val codeChallenges: CodeChallenges? = null,
    val honor: Int? = null,
    val leaderboardPosition: Int? = null,
    val name: String? = null,
    val ranks: Ranks? = null,
    val skills: List<String>? = null,
    @PrimaryKey
    val username: String = "",
    val datetime:Long
){
    fun bestLanguageAndPoints(): String {
        val best = ranks?.languages?.language?.maxBy { it.score  }
        return best?.let {
            "${it.languageName} - ${it.score}"
        } ?: ""
    }
}




