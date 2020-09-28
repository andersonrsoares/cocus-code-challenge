package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@OpenForTesting
class ListCompletedChallengeViewModel @Inject constructor(val repository: CompletedChallengeRepository) : BaseViewModel()  {

    private var _dataCompletedChallenge = MutableLiveData<List<CompletedChallenge>>()

    val dataCompletedChallenge:LiveData<List<CompletedChallenge>>
        get() = _dataCompletedChallenge


    private var totalPages:Int = 1
    private var currentPage:Int = 1

    fun listUserCompletedChallenge(username:String){
        _loading.postValue(true)
        disposable.add(repository
            .getCompletedChallenges(username,currentPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error))
    }

    fun nextPagePistUserCompletedChallenge(username:String){
        totalPages++
        if(currentPage < totalPages){
            listUserCompletedChallenge(username)
        }else{
            _message.postValue("end of list")//temporary refactor after
        }
    }

    private fun subscrible(result:PageCompletedChallenge){
        totalPages = result.totalPages ?: 1
        _dataCompletedChallenge.postValue(result.data)
        complete()
    }

}