package br.com.anderson.cocuscodechallenge.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.CodeChallengesTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.RanksTypeConverter
import com.google.gson.annotations.SerializedName


@Entity
@TypeConverters(value = [CodeChallengesTypeConverter::class,RanksTypeConverter::class,ListStringTypeConverter::class])
data class User(
    var clan: String = "",
    var codeChallenges: CodeChallenges? = null,
    var honor: Int = 0,
    var leaderboardPosition: Int = 0,
    var name: String = "",
    var ranks: Ranks? = null,
    var skills: List<String>? = null,
    @PrimaryKey
    var username: String = "",
    var datetime:Long
)




