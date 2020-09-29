package br.com.anderson.cocuscodechallenge.model


sealed class ViewState<out T> {

    data class Success<out T>(val value: T): ViewState<T>()

    object Empty : ViewState<Nothing>()

    object Retry : ViewState<Nothing>()

    object InternetConnection : ViewState<Nothing>()

    data class Error(val cause: String): ViewState<Nothing>()

}
