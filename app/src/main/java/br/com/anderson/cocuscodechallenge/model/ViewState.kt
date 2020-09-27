package br.com.anderson.cocuscodechallenge.model


sealed class ViewState<out T> {

    data class Success<out T>(val value: T): ViewState<T>()

    object Empty : ViewState<Nothing>()

    data class Error(
        val causeError: CauseError
    ): ViewState<Nothing>()


}

sealed class CauseError {

    object NetWorkTimeout : CauseError()

    object ServerError : CauseError()

    class Generic(val errorMessage: String? = null) : CauseError()

}