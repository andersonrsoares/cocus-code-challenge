package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.UserDTO
import br.com.anderson.cocuscodechallenge.model.User
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor(): Mapper<UserDTO, User>() {

    override fun map(from: UserDTO): User = User(
        datetime = Calendar.getInstance().timeInMillis,
        clan = from.clan, name = from.name, username = from.username,
        codeChallenges = from.codeChallenges, honor = from.honor,
        leaderboardPosition = from.leaderboardPosition,
        ranks = from.ranks, skills = from.skills
    )
}
