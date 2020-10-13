package br.com.anderson.cocuscodechallenge.model

sealed class ViewState {
    object Empty : ViewState()

    object Clean : ViewState()

    object Retry : ViewState()

    data class Loading(val value: Boolean) : ViewState()

    data class Message(val errorMessage: String) : ViewState()

    data class Data<T>(val body: T) : ViewState()
}
