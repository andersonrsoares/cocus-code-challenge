package br.com.anderson.cocuscodechallenge.repository


import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
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
        val localData = getOnlyPageOneLocalDataCompleteChallange(username, page)

        val remoteData = getRemotelDataCompleteChallange(username, page)

        return Flowable.merge(localData,remoteData).subscribeOn(Schedulers.io())
    }

    private fun getLocalDataCompleteChallange(username:String):Flowable<PageCompletedChallenge>{
       return localDataSouse.allCompletedChallenges(username)
            .subscribeOn(Schedulers.io())
            .flatMapPublisher {
                if(it.isNotEmpty()){
                    Flowable.just(PageCompletedChallenge(totalPages = 1,totalItems = it.size, data = it))
                }else{
                    Flowable.never()
                }
            }
    }

    fun getOnlyPageOneLocalDataCompleteChallange(username:String,page:Int): Flowable<PageCompletedChallenge>{
        return if(page==1){
            getLocalDataCompleteChallange(username)
        }else{
            Flowable.empty()
        }
    }

    private fun getRemotelDataCompleteChallange(username:String,page:Int):Flowable<PageCompletedChallenge>{
        return remoteDataSource.getCompletedChallenges(username,page)
            .subscribeOn(Schedulers.io())
            .map {
                it.toPageCompletedChallenge(username)
            }.doOnSuccess {
                it.data?.forEach {completed->
                    localDataSouse.insertCompletedChallenge(completed).subscribe()
                }
            }.toFlowable()
    }
}
