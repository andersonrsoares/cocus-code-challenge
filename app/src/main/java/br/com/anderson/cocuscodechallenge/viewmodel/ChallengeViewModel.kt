package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.repository.ChallengeRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@OpenForTesting
class ChallengeViewModel @Inject constructor(val repository: ChallengeRepository) : BaseViewModel()  {

    private var _dataChallenge = MutableLiveData<Challenge>()

    val dataChallenge:LiveData<Challenge>
        get() = _dataChallenge


    fun listChallenge(id:String){
        _loading.postValue(true)
        disposable.add(repository
            .getAuthoredChallenges(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error,this::complete) )
    }


    private fun subscrible(result:Challenge){
        _dataChallenge.postValue(result)
    }

}