package br.com.anderson.cocuscodechallenge.ui.listuser

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.model.ViewState
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import br.com.anderson.cocuscodechallenge.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OpenForTesting
class ListUserViewModel @Inject constructor(val repository: UserRepository) : ViewModel() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    protected val disposable = CompositeDisposable()

    protected var _newUser = MutableLiveData<ViewState<*>>()
    val newUser: LiveData<ViewState<*>>
        get() = _newUser

    private var _dataListLastUsers = MutableLiveData<ViewState<List<User>>>()

    val dataListLastUsers: LiveData<ViewState<List<User>>>
        get() = _dataListLastUsers

    private var currentListUser = arrayListOf<User>()

    fun listLastUsers() {
        if (currentListUser.isNotEmpty()) {
            return
        }

        _dataListLastUsers.postValue(ViewState.Loading(true))
        disposable.add(
            repository
                .listLastUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribleLastUsers, this::errorListLastUser)
        )
    }

    fun orderByPosition() {
        disposable.add(
            repository
                .listOrderByPosition()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _dataListLastUsers.postValue(ViewState.Clean)
                    _dataListLastUsers.postValue(ViewState.Success(it.body.orEmpty()))
                }
        )
    }

    fun orderByLookUp() {
        disposable.add(
            repository
                .listOrderByLookUp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _dataListLastUsers.postValue(ViewState.Empty)
                    _dataListLastUsers.postValue(ViewState.Success(it.body.orEmpty()))
                }
        )
    }

    fun searchUser(username: String?) {
        if (!validateSearchField(username)) {
            return
        }
        _newUser.value = ViewState.Loading(true)
        disposable.add(
            repository
                .searchUser(username ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribleNewUser, this::errorSearchUser, this::completeSearchUser)
        )
    }

    private fun validateSearchField(username: String?): Boolean {
        if (username.isNullOrBlank()) {
            _newUser.postValue(ViewState.Error(resourceProvider.getString(R.string.username_field_black)))
            return false
        } else {
            return true
        }
    }

    private fun subscribleNewUser(result: DataSourceResult<User>) {
        when {
            result.body != null -> {
                _dataListLastUsers.value  = ViewState.Clean
                delayToNewUser()
                _dataListLastUsers.value = ViewState.Success(replaceIfExists(result.body))
            }
            result.error is ErrorResult.NotFound -> _newUser.postValue(ViewState.Error(resourceProvider.getString(R.string.message_user_not_found)))
            result.error != null -> emitError(_newUser,result.error)
        }
        completeSearchUser()
    }

    private fun delayToNewUser() {
        disposable.add(
            Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _newUser.postValue(ViewState.Success(null))
                }
        )
    }

    private fun replaceIfExists(result: User?): List<User> {
        val list = currentListUser.take(5).toMutableList()
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
        _dataListLastUsers.value = ViewState.Success(result.body.orEmpty())
        if(result.body.isNullOrEmpty())
            _dataListLastUsers.value  = ViewState.Empty

        completeLastUser()
    }

    protected fun errorListLastUser(error: Throwable) {
        _dataListLastUsers.postValue(ViewState.Error(resourceProvider.getString(R.string.message_error)))
        error.printStackTrace()
        completeLastUser()
    }

    protected fun errorSearchUser(error: Throwable) {
        _newUser.postValue(ViewState.Error(resourceProvider.getString(R.string.message_error)))
        error.printStackTrace()
    }

    protected fun emitError(liveData: MutableLiveData<ViewState<*>>, error: ErrorResult) {
        when (error) {
            is ErrorResult.GenericError -> liveData.postValue(ViewState.Error(error.errorMessage))
            is ErrorResult.ServerError -> liveData.postValue(ViewState.Error(resourceProvider.getString(R.string.message_server_error)))
            is ErrorResult.NetworkError -> liveData.postValue(ViewState.Retry(resourceProvider.getString(R.string.message_server_network_error_retry)))
            is ErrorResult.NotFound -> liveData.postValue(ViewState.Error(resourceProvider.getString(R.string.message_not_found)))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        currentListUser.clear()

    }

    protected fun completeSearchUser() {
        _newUser.value  = ViewState.Loading(false)
    }

    protected fun completeLastUser() {
        _dataListLastUsers.value  = ViewState.Loading(false)
    }


}
