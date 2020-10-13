package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.DataSourceResult
import br.com.anderson.cocuscodechallenge.model.ErrorResult
import br.com.anderson.cocuscodechallenge.repository.ChallengeRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@OpenForTesting
class ChallengeViewModel @Inject constructor(val repository: ChallengeRepository) : BaseViewModel() {

    private var _dataChallenge = MutableLiveData<Challenge>()

    val dataChallenge: LiveData<Challenge>
        get() = _dataChallenge

    fun listChallenge(id: String?) {
        _loading.postValue(true)
        disposable.add(
            repository
                .getChallenge(id ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscrible, this::error, this::complete)
        )
    }

    private fun subscrible(result: DataSourceResult<Challenge>) {
        when {
            result.error is ErrorResult.NotFound -> _message.postValue(resourceProvider.getString(R.string.message_challenge_not_found))
            result.body != null -> _dataChallenge.postValue(result.body)
        }
        complete()
    }
}
