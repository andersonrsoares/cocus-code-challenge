package br.com.anderson.cocuscodechallenge.extras


import br.com.anderson.cocuscodechallenge.dto.BaseDTO
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException



fun <T> Single<T>.transformToDataSourceResult(): Single<DataSourceResult<T>> {
   return this.map { DataSourceResult.create(it) }.onErrorReturn { it.createDataSourceResult() }
}

fun <T> Maybe<T>.transformToDataSourceResult(): Maybe<DataSourceResult<T>> {
    return this.map { DataSourceResult.create(it) }.onErrorReturn { it.createDataSourceResult() }
}

fun <T> Throwable.createDataSourceResult(): DataSourceResult<T>{
    return DataSourceResult.error(this)
}

fun Throwable.handleException(): ErrorResult {
     when(this){
         is UnknownHostException, is TimeoutException, is TimeoutCancellationException, is IOException -> ErrorResult.NetworkError
         is MalformedJsonException -> ErrorResult.ServerError
         is HttpException -> this.handleServerError()
         else -> ErrorResult.GenericError()

     }

     return ErrorResult.NetworkError
}

fun HttpException.handleServerError(): ErrorResult {
    val message = extractMessage()
    return when(this.code()){
        404 -> ErrorResult.NotFound
        else -> ErrorResult.GenericError(message)
    }
}

fun HttpException.extractMessage():String {
   return try {
       Gson().fromJson(this.response()?.errorBody()?.string(),BaseDTO::class.java).reason ?: ""
   } catch (e:Exception){
        ""
   }
}

