package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.CompletedChallengeAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.hideKeyboard
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.viewmodel.ListCompletedChallengeViewModel
import kotlinx.android.synthetic.main.fragment_list_completed_challenge.*
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
        initrRefresh()
        initScrollListener()
        initObservers()
        fetchCompletedChallenges()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = UserDetailFragmentArgs.fromBundle(it)
        }
    }

    private fun initrRefresh(){
        swiperefresh.setOnRefreshListener(this::onRefresh)
    }

    fun onRefresh(){
        viewModel.refresh()
    }

    private fun fetchCompletedChallenges(){
        viewModel.listUserCompletedChallenge(args?.username ?: "")
    }

    private fun initObservers(){
        observe(viewModel.dataCompletedChallenge,this::onLoadDataCompletedChallenge)
        observe(viewModel.message,this::onMessage)
        observe(viewModel.loading,this::onLoading)
        observe(viewModel.clean,this::onClean)
    }

    private fun initRecycleView(){
        adapter = CompletedChallengeAdapter()
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
        adapter.itemOnClick = this::onItemClick
    }

    private fun onItemClick(challange: CompletedChallenge){
        (parentFragment as? UserDetailFragment)?.navigateToChallenge(challange.id)
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
        progressloadingnextpage.isVisible = data && adapter.currentList.isNotEmpty()
        swiperefresh.isRefreshing = data && adapter.currentList.isEmpty()
    }


    private fun onClean(data: Boolean) {
       adapter.submitList(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) =
            ListCompletedChallengeFragment().apply {
                arguments = args
            }
    }
}