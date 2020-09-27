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
    var clan: String? = null,
    var codeChallenges: CodeChallenges? = null,
    var honor: Int? = null,
    var leaderboardPosition: Int? = null,
    var name: String? = null,
    var ranks: Ranks? = null,
    var skills: List<String>? = null,
    @PrimaryKey
    var username: String = "",
    var datetime:Long
){
    fun bestLanguageAndPoints(): String {
        val best = ranks?.languages?.language?.maxBy { it.score  }
        return best?.let {
            "${it.languageName} - ${it.score}"
        } ?: ""
    }
}




