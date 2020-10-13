package br.com.anderson.cocuscodechallenge.repository

import br.com.anderson.cocuscodechallenge.extras.transformToDataSourceResult
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class CompletedChallengeRepository @Inject constructor(
    val localDataSouse: CodeWarsDao,
    val remoteDataSource: CodeWarsService
) {

    fun getCompletedChallenges(username: String, page: Int): Flowable<DataSourceResult<PageCompletedChallenge>> {
        val localData = getOnlyPageOneLocalDataCompleteChallange(username, page)

        val remoteData = getRemotelDataCompleteChallange(username, page)

        return Flowable.merge(localData, remoteData).subscribeOn(Schedulers.io())
    }

    private fun getLocalDataCompleteChallange(username: String): Flowable<DataSourceResult<PageCompletedChallenge>> {
        return localDataSouse.allCompletedChallenges(username)
            .subscribeOn(Schedulers.io())
            .flatMap {
                if (it.isNotEmpty()) {
                    Single.just(PageCompletedChallenge(totalPages = 1, totalItems = it.size, data = it))
                } else {
                    Single.never()
                }
            }
            .transformToDataSourceResult()
            .toFlowable()
    }

    fun getOnlyPageOneLocalDataCompleteChallange(username: String, page: Int): Flowable<DataSourceResult<PageCompletedChallenge>> {
        return if (page == 1) {
            getLocalDataCompleteChallange(username)
        } else {
            Flowable.empty()
        }
    }

    private fun getRemotelDataCompleteChallange(username: String, page: Int): Flowable<DataSourceResult<PageCompletedChallenge>> {
        return remoteDataSource.getCompletedChallenges(username, page)
            .subscribeOn(Schedulers.io())
            .map {
                it.toPageCompletedChallenge(username)
            }
            .doOnSuccess {
                it.data?.forEach { completed ->
                    localDataSouse.insertCompletedChallenge(completed).subscribe()
                }
            }.transformToDataSourceResult()
            .toFlowable()
    }
}
