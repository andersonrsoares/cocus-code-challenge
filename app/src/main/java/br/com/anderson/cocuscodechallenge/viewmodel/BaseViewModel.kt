package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.disposables.CompositeDisposable


@OpenForTesting
open class BaseViewModel : ViewModel()  {

    protected val disposable = CompositeDisposable()
    protected var _message = MutableLiveData<String>()
    protected var _loading = MutableLiveData<Boolean>()

    val message:LiveData<String>
        get() = _message

    val loading:LiveData<Boolean>
        get() = _loading

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    protected fun complete(){
        _loading.postValue(false)
    }

    protected fun error(error:Throwable){
        _message.postValue("erro")
        complete()
        error.printStackTrace()
    }

}