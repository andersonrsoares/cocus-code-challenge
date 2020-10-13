package br.com.anderson.cocuscodechallenge.repository

import br.com.anderson.cocuscodechallenge.extras.transformToDataSourceResult
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class ChallengeRepository @Inject constructor(
    val localDataSouse: CodeWarsDao,
    val remoteDataSource: CodeWarsService
) {

    fun getChallenge(id: String): Flowable<DataSourceResult<Challenge>> {
        val localData = getRemoteDataChallenge(id)
        val remoteData = getLocalDataChallange(id)

        return Flowable.merge(localData, remoteData).subscribeOn(Schedulers.io())
    }

    private fun getRemoteDataChallenge(id: String): Flowable<DataSourceResult<Challenge>> {
        return remoteDataSource.getChallenge(id)
            .subscribeOn(Schedulers.io())
            .map {
                it.toChallange()
            }.doOnSuccess {
                localDataSouse.insertChallenge(it).subscribe()
            }.transformToDataSourceResult().toFlowable()
    }

    private fun getLocalDataChallange(id: String): Flowable<DataSourceResult<Challenge>> {
        return localDataSouse.getChallenge(id)
            .subscribeOn(Schedulers.io())
            .transformToDataSourceResult()
            .toFlowable()
    }
}
