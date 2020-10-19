package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.PageCompletedChallengeDTO
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge

class PageCompletedChallengeMapper(private val completedChallengeMapper: CompletedChallengeMapper) : Mapper<PageCompletedChallengeDTO, PageCompletedChallenge>() {

    override fun map(from: PageCompletedChallengeDTO): PageCompletedChallenge = PageCompletedChallenge(totalItems = from.totalItems,
        data = from.data?.map { completedChallengeMapper.map(it)}, totalPages = from.totalPages)

}