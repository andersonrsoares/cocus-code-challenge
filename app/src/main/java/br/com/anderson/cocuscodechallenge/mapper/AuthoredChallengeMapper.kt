package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.AuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthoredChallengeMapper @Inject constructor() : Mapper<AuthoredChallengeDTO, AuthoredChallenge>() {

    override fun map(from: AuthoredChallengeDTO): AuthoredChallenge = AuthoredChallenge(
        rank = from.rank,
        rankName = from.rankName, id = from.id.toString(), name = from.name,
        description = from.description, tags = from.tags.orEmpty(), languages = from.languages.orEmpty()
    )
}
