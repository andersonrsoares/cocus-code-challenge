package br.com.anderson.cocuscodechallenge.ui.listcompleted

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.CompletedChallengeAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.ui.userdetail.UserDetailFragment
import br.com.anderson.cocuscodechallenge.ui.userdetail.UserDetailFragmentArgs
import kotlinx.android.synthetic.main.fragment_list_completed_challenge.*
import javax.inject.Inject

class ListCompletedChallengeFragment : Fragment(R.layout.fragment_list_completed_challenge), Injectable {

    lateinit var adapter: CompletedChallengeAdapter

    val viewModel: ListCompletedChallengeViewModel by viewModels {
        factory
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    var args: UserDetailFragmentArgs? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initrRefresh()
        initRetryButton()
        initScrollListener()
        initObservers()
        fetchCompletedChallenges()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args =
                UserDetailFragmentArgs.fromBundle(
                    it
                )
        }
    }

    private fun initRetryButton() {
        retrybutton.setOnClickListener(this::onRetryClick)
    }

    fun onRetryClick(view: View) {
        viewModel.refresh()
        view.isVisible = false
    }

    private fun initrRefresh() {
        swiperefresh.setOnRefreshListener(this::onRefresh)
    }

    fun onRefresh() {
        viewModel.refresh()
    }

    private fun fetchCompletedChallenges() {
        viewModel.listUserCompletedChallenge(args?.username ?: "")
    }

    private fun initObservers() {
        observe(viewModel.dataCompletedChallenge, this::onLoadDataCompletedChallenge)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
        observe(viewModel.clean, this::onClean)
        observe(viewModel.retry, this::onRetry)
    }

    private fun initRecycleView() {
        adapter = CompletedChallengeAdapter()
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
        adapter.itemOnClick = this::onItemClick
    }

    private fun onItemClick(challange: CompletedChallenge) {
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
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        progressloadingnextpage.isVisible = data && adapter.currentList.isNotEmpty()
        swiperefresh.isRefreshing = data && adapter.currentList.isEmpty()
    }

    private fun onClean(data: Boolean) {
        if (data)
            adapter.submitList(arrayListOf())
    }

    private fun onRetry(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
        retrybutton.isVisible = true
    }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) =
            ListCompletedChallengeFragment()
                .apply {
                    arguments = args
                }
    }
}
