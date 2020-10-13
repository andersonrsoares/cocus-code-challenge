package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@OpenForTesting
class ListUserViewModel @Inject constructor(val repository: UserRepository) : BaseViewModel() {

    private var _dataListLastUsers = MutableLiveData<List<User>>()

    val dataListLastUsers: LiveData<List<User>>
        get() = _dataListLastUsers

    fun listLastUsers() {
        _loading.postValue(true)
        disposable.add(
            repository
                .listLastUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribleLastUsers, this::error)
        )
    }

    fun searchUser(username: String) {
        if (!validateSearchField(username)) {
            return
        }
        _loading.postValue(true)
        disposable.add(
            repository
                .searchUser(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribleNewUser, this::error, this::complete)
        )
    }

    private fun validateSearchField(username: String): Boolean {
        if (username.isEmpty()) {
            _message.postValue(resourceProvider.getString(R.string.username_field_black))
            return false
        } else {
            return true
        }
    }

    private fun subscribleNewUser(result: DataSourceResult<User>) {
        when {
            result.body != null -> _dataListLastUsers.postValue(replaceIfExists(result.body))
            result.error is ErrorResult.NotFound -> _message.postValue(resourceProvider.getString(R.string.message_user_not_found))
            result.error != null -> error(result.error)
        }
        complete()
    }

    private fun replaceIfExists(result: User?): List<User> {
        val list = _dataListLastUsers.value.orEmpty().toMutableList()
        result?.let {
            val index = list.indexOfFirst { user -> user.username == result.username }
            if (index >= 0) {
                list.removeAt(index)
            }
            list.add(0, result)
        }
        return list
    }

    private fun subscribleLastUsers(result: DataSourceResult<List<User>>) {
        _dataListLastUsers.postValue(result.body)
        complete()
    }
}
