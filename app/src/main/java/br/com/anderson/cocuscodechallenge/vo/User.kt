package br.com.anderson.cocuscodechallenge.vo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.CodeChallengesTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.ListStringTypeConverter
import br.com.anderson.cocuscodechallenge.persistence.typeconverters.RanksTypeConverter
import com.google.gson.annotations.SerializedName


open class BaseModel{
    @Ignore
    @SerializedName("reason")
    var reason: String? = null
    @Ignore
    @SerializedName("success")
    var success: Boolean? = null
}

@Entity
@TypeConverters(value = [CodeChallengesTypeConverter::class,RanksTypeConverter::class,ListStringTypeConverter::class])
data class User(
    @SerializedName("clan")
    var clan: String = "",
    @SerializedName("codeChallenges")
    var codeChallenges: CodeChallenges? = null,
    @SerializedName("honor")
    var honor: Int = 0,
    @SerializedName("leaderboardPosition")
    var leaderboardPosition: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("ranks")
    var ranks: Ranks? = null,
    @SerializedName("skills")
    var skills: List<String>? = null,
    @PrimaryKey
    @SerializedName("username")
    var username: String = ""
) : BaseModel()


data class CodeChallenges(
    @SerializedName("totalAuthored")
    var totalAuthored: Int = 0,
    @SerializedName("totalCompleted")
    var totalCompleted: Int = 0
)

data class Ranks(
    @SerializedName("languages")
    var languages: Languages? = null,
    @SerializedName("overall")
    var overall: Overall? = null
)

data class Languages(
    var language: HashMap<String,Language>? = null
)

open class Language(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("rank")
    var rank: Int = 0,
    @SerializedName("score")
    var score: Int = 0
)

class Overall : Language()