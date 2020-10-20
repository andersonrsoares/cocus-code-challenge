package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.ChallengeDTO
import br.com.anderson.cocuscodechallenge.extras.toTimestamp
import br.com.anderson.cocuscodechallenge.model.Challenge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeMapper @Inject constructor() : Mapper<ChallengeDTO, Challenge>() {

    override fun map(from: ChallengeDTO): Challenge = Challenge(
        publishedAt = from.publishedAt?.toTimestamp(),
        languages = from.languages, tags = from.tags, description = from.description,
        id = from.id.toString(), rank = from.rank, slug = from.slug, name = from.name,
        approvedAt = from.approvedAt?.toTimestamp(), approvedBy = from.approvedBy, category = from.category,
        createdBy = from.createdBy, totalAttempts = from.totalAttempts, totalCompleted = from.totalCompleted,
        totalStars = from.totalStars, url = from.url
    )
}
