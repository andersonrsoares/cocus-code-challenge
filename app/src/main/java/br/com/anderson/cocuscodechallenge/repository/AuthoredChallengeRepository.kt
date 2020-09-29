package br.com.anderson.cocuscodechallenge.repository


import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
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
class AuthoredChallengeRepository @Inject constructor(val localDataSouse: CodeWarsDao,
                                                      val remoteDataSource:CodeWarsService) {


    fun getAuthoredChallenges(username:String):Flowable<List<AuthoredChallenge>>{
        val localData = getLocalDataAuthoredChallange(username)
        val remoteData = getRemoteDataAuthoredChallenge(username)

        return Flowable.merge(localData,remoteData).subscribeOn(Schedulers.io())
    }

    private fun getRemoteDataAuthoredChallenge(username:String):Flowable<List<AuthoredChallenge>>{
       return remoteDataSource.getAuthoredChallenges(username)
            .subscribeOn(Schedulers.io())
            .map {
                it.toAuthoredChallengeList(username)
            }.doOnSuccess {
                it.forEach { completed ->
                    localDataSouse.insertAuthoredChallenge(completed).subscribe()
                }
            }.onErrorResumeNext {
               Single.just(arrayListOf())
           }.toFlowable()
    }

    private fun getLocalDataAuthoredChallange(username:String):Flowable<List<AuthoredChallenge>>{
      return localDataSouse.allAuthoredChallenges(username)
           .subscribeOn(Schedulers.io())
          .flatMapPublisher {
              if(it.isNotEmpty()){
                  Flowable.just(it)
              }else{
                  Flowable.never()
              }
          }
   }
}
