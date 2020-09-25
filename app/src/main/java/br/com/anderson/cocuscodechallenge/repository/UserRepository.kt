package br.com.anderson.cocuscodechallenge.repository

import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.services.CodeWarsService
import br.com.anderson.cocuscodechallenge.vo.User
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(localDataSouse: CodeWarsDao,remoteDataSource:CodeWarsService) {

}
