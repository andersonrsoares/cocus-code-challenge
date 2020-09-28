package br.com.anderson.cocuscodechallenge.repository


import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class CompletedChallengeRepository @Inject constructor(val localDataSouse: CodeWarsDao,
                                                       val remoteDataSource:CodeWarsService) {


    fun getCompletedChallenges(username:String,page:Int):Flowable<PageCompletedChallenge>{
        val localData = localDataSouse.allCompletedChallenges(username)
            .subscribeOn(Schedulers.io())
            .map {
                PageCompletedChallenge(totalPages = 1,totalItems = it.size, data = it)
            }.toFlowable()

        val remoteData = remoteDataSource.getCompletedChallenges(username,page)
            .subscribeOn(Schedulers.io())
            .map {
                it.toPageCompletedChallenge()
            }.doOnSuccess {
                 it.data?.forEach {completed->
                     localDataSouse.insertCompletedChallenge(completed).subscribe()
                 }
            }.toFlowable()

        return Flowable.merge(localData,remoteData).subscribeOn(Schedulers.io())
    }
}
