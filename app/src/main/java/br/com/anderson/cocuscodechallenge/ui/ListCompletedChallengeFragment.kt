package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.ListUserAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.viewmodel.ListCompletedChallengeViewModel
import br.com.anderson.cocuscodechallenge.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.fragment_list_user.*
import javax.inject.Inject


class ListCompletedChallengeFragment : Fragment(R.layout.fragment_list_completed_challenge), Injectable{
    
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //lateinit var adapter:ListUserAdapter

    @Inject
    lateinit var viewModel: ListCompletedChallengeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        fetchCompletedChallenges()
    }

    private fun fetchCompletedChallenges(){
        viewModel.listUserCompletedChallenge("g964")
    }

    private fun initObservers(){
        observe(viewModel.dataCompletedChallenge,this::onLoadDataCompletedChallenge)
        observe(viewModel.message,this::onMessage)
        observe(viewModel.loading,this::onLoading)
    }

    private fun initRecycleView(){
//        adapter = ListUserAdapter()
//        recycleview.adapter = adapter
//        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
//            orientation = LinearLayoutManager.VERTICAL
//        }
//        recycleview.setDivider(R.drawable.divider_recycleview)
    }

    private fun onLoadDataCompletedChallenge(data: List<CompletedChallenge>) {
        //adapter.submitList(data)
        print(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(),data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        progressloading.isVisible = data
    }

}