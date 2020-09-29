package br.com.anderson.cocuscodechallenge.model



sealed class DataState<out T> {

    data class Success<out T>(val value: T): DataState<T>()

    object NetworkError : DataState<Nothing>()

    data class GenericError(val errorMessage: String? = null): DataState<Nothing>()

}