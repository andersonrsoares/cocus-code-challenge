package br.com.anderson.cocuscodechallenge.repository


import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserRepository @Inject constructor(val localDataSouse: CodeWarsDao,
                                         val remoteDataSource:CodeWarsService) {


    fun listLastUsers():Flowable<List<User>>{
        return localDataSouse.allUsers()
            .subscribeOn(Schedulers.io())
            .limit(5)
    }

    fun searchUser(username:String):Flowable<User>{
      return remoteDataSource.getUser(username)
            .subscribeOn(Schedulers.io())
            .map {
               it.toUser()
            }.doOnSuccess {
              localDataSouse.insertUser(user = it)
            }.toFlowable()
    }
}
