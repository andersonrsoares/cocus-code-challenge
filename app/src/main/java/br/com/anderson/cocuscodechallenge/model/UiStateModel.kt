package br.com.anderson.cocuscodechallenge.model

sealed class UiStateModel{
    data class Data<T>(val data: T?) : UiStateModel()
    data class Error(val message: String) : UiStateModel()
}


