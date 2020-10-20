package br.com.anderson.cocuscodechallenge.repository

import br.com.anderson.cocuscodechallenge.extras.transformToDataSourceResult
import br.com.anderson.cocuscodechallenge.mapper.UserMapper
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    val localDataSouse: CodeWarsDao,
    val remoteDataSource: CodeWarsService,
    val userMapper: UserMapper
) {
    fun listLastUsers(): Flowable<DataSourceResult<List<User>>> {
        return localDataSouse.allUsers()
            .transformToDataSourceResult()
            .subscribeOn(Schedulers.io()).toFlowable()
    }

    fun listOrderByPosition(): Flowable<DataSourceResult<List<User>>> {
        return localDataSouse.allUsers()
            .map {
                it.sortedBy { item -> item.leaderboardPosition }
            }
            .transformToDataSourceResult()
            .subscribeOn(Schedulers.io()).toFlowable()
    }

    fun listOrderByLookUp(): Flowable<DataSourceResult<List<User>>> {
        return localDataSouse.allUsers()
            .map {
                it.sortedByDescending { item -> item.datetime }
            }
            .transformToDataSourceResult()
            .subscribeOn(Schedulers.io()).toFlowable()
    }

    fun searchUser(username: String): Flowable<DataSourceResult<User>> {
        return remoteDataSource.getUser(username)
            .subscribeOn(Schedulers.io())
            .map {
                userMapper.map(it)
            }
            .doOnSuccess {
                localDataSouse.insertUser(user = it).subscribe()
            }.transformToDataSourceResult().toFlowable()
    }
}
