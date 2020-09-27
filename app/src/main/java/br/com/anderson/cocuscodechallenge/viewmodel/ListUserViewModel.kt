package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@OpenForTesting
class ListUserViewModel @Inject constructor(val resourceProvider: ResourceProvider,val repository: UserRepository) : BaseViewModel()  {

    private var _dataListLastUsers = MutableLiveData<List<User>>()

    val dataListLastUsers:LiveData<List<User>>
        get() = _dataListLastUsers


    private var _dataSearchUser = MutableLiveData<User>()

    val dataSearchUser:LiveData<User>
        get() = _dataSearchUser

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun listLastUsers(){
        _loading.postValue(true)
        disposable.add(repository
            .listLastUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error))
    }

    fun searchUser(username:String){
        if(!validateSearchField(username)){
            return
        }
        _loading.postValue(true)
        disposable.add(repository
            .searchUser(username)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error))

    }

    private fun validateSearchField(username:String):Boolean{
        if(username.isEmpty()){
            _message.postValue(resourceProvider.getString(R.string.username_field_black))
            return false
        }else{
            return true
        }
    }

    private fun subscrible(result:User){
        _dataSearchUser.postValue(result)
        complete()
    }

    private fun subscrible(result:List<User>){
        _dataListLastUsers.postValue(result)
        complete()
    }

    private fun complete(){
        _loading.postValue(false)
    }

    private fun error(error:Throwable){
        _message.postValue("erro")
        complete()
        error.printStackTrace()
    }
}