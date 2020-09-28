package br.com.anderson.cocuscodechallenge.dto

import androidx.room.PrimaryKey
import br.com.anderson.cocuscodechallenge.model.CodeChallenges
import br.com.anderson.cocuscodechallenge.model.Ranks
import br.com.anderson.cocuscodechallenge.model.User
import com.google.gson.annotations.SerializedName
import java.util.*


data class UserDTO(
    @SerializedName("clan")
    val clan: String = "",
    @SerializedName("codeChallenges")
    val codeChallenges: CodeChallenges? = null,
    @SerializedName("honor")
    val honor: Int = 0,
    @SerializedName("leaderboardPosition")
    val leaderboardPosition: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("ranks")
    val ranks: Ranks? = null,
    @SerializedName("skills")
    val skills: List<String>? = null,
    @PrimaryKey
    @SerializedName("username")
    val username: String = ""
):BaseDTO(){
    fun toUser(): User {
        return User(datetime = Calendar.getInstance().timeInMillis,
                    clan = clan, name = name, username = username,
                    codeChallenges = codeChallenges, honor = honor,
                    leaderboardPosition = leaderboardPosition,
                    ranks = ranks,skills = skills)
    }
}
