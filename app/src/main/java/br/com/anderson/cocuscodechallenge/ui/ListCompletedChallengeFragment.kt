package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.CompletedChallengeAdapter
import br.com.anderson.cocuscodechallenge.adapter.ListUserAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.viewmodel.ListCompletedChallengeViewModel
import br.com.anderson.cocuscodechallenge.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.fragment_list_completed_challenge.*
import kotlinx.android.synthetic.main.fragment_list_user.*
import kotlinx.android.synthetic.main.fragment_list_user.progressloading
import kotlinx.android.synthetic.main.fragment_list_user.recycleview
import javax.inject.Inject


class ListCompletedChallengeFragment : Fragment(R.layout.fragment_list_completed_challenge), Injectable{
    
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter:CompletedChallengeAdapter

    @Inject
    lateinit var viewModel: ListCompletedChallengeViewModel


    var args: UserDetailFragmentArgs? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initScrollListener()
        initObservers()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = UserDetailFragmentArgs.fromBundle(it)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchCompletedChallenges()
    }

    private fun fetchCompletedChallenges(){
        viewModel.listUserCompletedChallenge(getUsername())
    }

    fun getUsername() = args?.username ?: ""

    private fun initObservers(){
        observe(viewModel.dataCompletedChallenge,this::onLoadDataCompletedChallenge)
        observe(viewModel.message,this::onMessage)
        observe(viewModel.loading,this::onLoading)
    }

    private fun initRecycleView(){
        adapter = CompletedChallengeAdapter()
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
    }

    private fun initScrollListener() {
        val layoutManager = recycleview.layoutManager as LinearLayoutManager
        recycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    private fun onLoadDataCompletedChallenge(data: List<CompletedChallenge>) {
        println("data - print onLoadDataCompletedChallenge")
        adapter.submitList(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(),data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        progressloading.isVisible = data && adapter.currentList.isEmpty()
        progressloadingnextpage.isVisible = data && adapter.currentList.isNotEmpty()
    }


    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) =
            ListCompletedChallengeFragment().apply {
                arguments = args
            }
    }
}