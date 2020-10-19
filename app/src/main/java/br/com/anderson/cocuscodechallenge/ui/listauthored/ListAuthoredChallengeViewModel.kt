package br.com.anderson.cocuscodechallenge.ui.listauthored

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.repository.AuthoredChallengeRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import br.com.anderson.cocuscodechallenge.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@OpenForTesting
class ListAuthoredChallengeViewModel @Inject constructor(val repository: AuthoredChallengeRepository) : BaseViewModel() {

    private var _dataAuthoredChallenge = MutableLiveData<List<AuthoredChallenge>>()

    val dataAuthoredChallenge: LiveData<List<AuthoredChallenge>>
        get() = _dataAuthoredChallenge

    var _username: String = ""
    fun listUserAuthoredChallenge(username: String?) {
        _username = username ?: ""
        _loading.postValue(true)
        disposable.add(
            repository
                .getAuthoredChallenges(_username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscrible, this::error, this::complete)
        )
    }

    private fun subscrible(result: DataSourceResult<List<AuthoredChallenge>>) {
        when {
            result.body != null -> emitList(result.body)
            result.error != null -> error(result.error)
        }
        complete()
    }

    private fun emitList(result: List<AuthoredChallenge>) {
        if (result.isNotEmpty()) {
            _dataAuthoredChallenge.postValue(result)
        }
    }
    override fun refresh() {
        super.refresh()
        listUserAuthoredChallenge(_username)
    }
}
