package br.com.anderson.cocuscodechallenge.repository

import br.com.anderson.cocuscodechallenge.extras.transformToDataSourceResult
import br.com.anderson.cocuscodechallenge.mapper.DataAuthoredChallengeMapper
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthoredChallengeRepository @Inject constructor(
    val localDataSouse: CodeWarsDao,
    val remoteDataSource: CodeWarsService,
    val dataAuthoredChallengeMapper: DataAuthoredChallengeMapper
) {

    fun getAuthoredChallenges(username: String): Flowable<DataSourceResult<List<AuthoredChallenge>>> {
        val localData = getLocalDataAuthoredChallange(username)
        val remoteData = getRemoteDataAuthoredChallenge(username)

        return Flowable.merge(localData, remoteData).subscribeOn(Schedulers.io())
    }

    private fun getRemoteDataAuthoredChallenge(username: String): Flowable<DataSourceResult<List<AuthoredChallenge>>> {
        return remoteDataSource.getAuthoredChallenges(username)
            .subscribeOn(Schedulers.io())
            .map {
                dataAuthoredChallengeMapper.map(it).apply {
                    this.forEach { f -> f.username = username }
                }
            }
            .doOnSuccess {
                it.forEach { completed ->
                    localDataSouse.insertAuthoredChallenge(completed).subscribe()
                }
            }.transformToDataSourceResult()
            .toFlowable()
    }

    private fun getLocalDataAuthoredChallange(username: String): Flowable<DataSourceResult<List<AuthoredChallenge>>> {
        return localDataSouse.allAuthoredChallenges(username)
            .subscribeOn(Schedulers.io())
            .flatMap {
                if (it.isNotEmpty()) {
                    Single.just(it)
                } else {
                    Single.never()
                }
            }
            .transformToDataSourceResult()
            .toFlowable()
    }
}
