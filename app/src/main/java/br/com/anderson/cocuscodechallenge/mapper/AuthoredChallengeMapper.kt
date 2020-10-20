package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.AuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge

class AuthoredChallengeMapper : Mapper<AuthoredChallengeDTO, AuthoredChallenge>() {

    override fun map(from: AuthoredChallengeDTO): AuthoredChallenge = AuthoredChallenge(
        rank = from.rank,
        rankName = from.rankName, id = from.id.toString(), name = from.name,
        description = from.description, tags = from.tags.orEmpty(), languages = from.languages.orEmpty()
    )
}
