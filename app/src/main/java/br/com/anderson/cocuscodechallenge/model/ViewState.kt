package br.com.anderson.cocuscodechallenge.model

sealed class ViewState<out T> {
    object Empty : ViewState<Nothing>()

    object Clean : ViewState<Nothing>()

    data class Retry(val message: String) : ViewState<Nothing>()

    data class Loading(val value: Boolean) : ViewState<Nothing>()

    data class Error(val errorMessage: String) : ViewState<Nothing>()

    data class Success<out T>(val value: T): ViewState<T>()
}
