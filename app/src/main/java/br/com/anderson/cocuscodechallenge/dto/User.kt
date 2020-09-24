package br.com.anderson.cocuscodechallenge.dto
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("clan")
    var clan: String = "",
    @SerializedName("codeChallenges")
    var codeChallenges: CodeChallenges = CodeChallenges(),
    @SerializedName("honor")
    var honor: Int = 0,
    @SerializedName("leaderboardPosition")
    var leaderboardPosition: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("ranks")
    var ranks: Ranks = Ranks(),
    @SerializedName("skills")
    var skills: List<String> = listOf(),
    @SerializedName("username")
    var username: String = ""
)

data class CodeChallenges(
    @SerializedName("totalAuthored")
    var totalAuthored: Int = 0,
    @SerializedName("totalCompleted")
    var totalCompleted: Int = 0
)

data class Ranks(
    @SerializedName("languages")
    var languages: Languages = Languages(),
    @SerializedName("overall")
    var overall: Overall = Overall()
)

data class Languages(
    var language: HashMap<String,Language> = HashMap()
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