package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.AuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.dto.UserDTO
import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import java.util.Calendar

class AuthoredChallengeMapper : Mapper<AuthoredChallengeDTO, AuthoredChallenge>() {

    override fun map(from: AuthoredChallengeDTO): AuthoredChallenge = AuthoredChallenge(rank = from.rank,
        rankName = from.rankName, id = from.id.toString(), name = from.name,
        description = from.description, tags = from.tags.orEmpty(), languages = from.languages.orEmpty())


}