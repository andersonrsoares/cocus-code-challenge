package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@OpenForTesting
class ListUserViewModel @Inject constructor(val repository: UserRepository) : BaseViewModel()  {

    private var _data = MutableLiveData<List<User>>()

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun listLastUsers(): LiveData<List<User>>{
        _loading.postValue(true)
        disposable.add(repository
            .listLastUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error,this::complete))

        return _data
    }

    private fun subscrible(result:List<User>){
        _data.postValue(result)
    }

    private fun complete(){
        _loading.postValue(false)
    }

    private fun error(error:Throwable){
        _message.postValue("erro") //fist test to unit test erros
        error.printStackTrace()
    }
}