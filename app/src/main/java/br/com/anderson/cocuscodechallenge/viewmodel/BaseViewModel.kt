package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class BaseViewModel : ViewModel()  {

    protected var _message = MutableLiveData<String>()
    protected var _loading = MutableLiveData<Boolean>()

    val message:LiveData<String>
        get() = _message

    val loading:LiveData<Boolean>
        get() = _loading

}