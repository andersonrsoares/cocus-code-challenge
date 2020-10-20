package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.CompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompletedChallengeMapper @Inject constructor() : Mapper<CompletedChallengeDTO, CompletedChallenge>() {

    override fun map(from: CompletedChallengeDTO): CompletedChallenge = CompletedChallenge(
        completedAt = from.completedAt.toTimestamp(),
        name = from.name, completedLanguages = from.completedLanguages, id = from.id, slug = from.slug
    )
}
