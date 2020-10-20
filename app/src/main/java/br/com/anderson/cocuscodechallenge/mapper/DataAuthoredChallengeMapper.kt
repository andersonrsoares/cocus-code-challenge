package br.com.anderson.cocuscodechallenge.mapper

import br.com.anderson.cocuscodechallenge.dto.DataAuthoredChallengeDTO
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataAuthoredChallengeMapper @Inject constructor(private val authoredChallengeMapper: AuthoredChallengeMapper) : Mapper<DataAuthoredChallengeDTO, List<AuthoredChallenge>>() {

    override fun map(from: DataAuthoredChallengeDTO): List<AuthoredChallenge> = from.data?.map { authoredChallengeMapper.map(it) }.orEmpty()
}
