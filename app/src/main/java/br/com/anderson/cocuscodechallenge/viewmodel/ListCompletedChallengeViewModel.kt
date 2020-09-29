package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.PageCompletedChallenge
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import br.com.anderson.cocuscodechallenge.repository.CompletedChallengeRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val VISIBLE_THRESHOLD = 5

@OpenForTesting
class ListCompletedChallengeViewModel @Inject constructor(val repository: CompletedChallengeRepository) : BaseViewModel()  {

    private var _dataCompletedChallenge = MutableLiveData<List<CompletedChallenge>>()

    val dataCompletedChallenge:LiveData<List<CompletedChallenge>>
        get() = _dataCompletedChallenge


    private var totalPages:Int = 1
    private var currentPage:Int = 1

    private var _username: String = ""

   override fun refresh(){
       super.refresh()
       totalPages = 1
       currentPage = 1
       listUserCompletedChallenge(_username)
    }

    fun listUserCompletedChallenge(username:String?){
        _username = username ?: ""
        _loading.postValue(true)
        disposable.add(repository
            .getCompletedChallenges(_username,currentPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::subscrible,this::error,this::complete) )
    }

    fun nextPageListUserCompletedChallenge(){
        if(_loading.value == false){
            currentPage++
            if(currentPage < totalPages){
                listUserCompletedChallenge(_username)
            }else{
                _message.postValue("end of list")//temporary refactor after
            }
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = dataCompletedChallenge.value
            if (immutableQuery != null) {
                nextPageListUserCompletedChallenge()
            }
        }
    }

    private fun subscrible(result:PageCompletedChallenge){
        if(!result.data.isNullOrEmpty()){
            totalPages = result.totalPages ?: 1
            _dataCompletedChallenge.postValue(appendToCurrent(result))
        }
        complete()
    }

    private fun appendToCurrent(result:PageCompletedChallenge):List<CompletedChallenge>{
        val list = _dataCompletedChallenge.value.orEmpty().toMutableList()
        if(currentPage == 1){
            list.clear()
            _clean.postValue(true)
        }
        list.addAll(result.data.orEmpty())
        return list
    }

}