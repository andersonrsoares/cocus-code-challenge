package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.AuthoredChallengeAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.hideKeyboard
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.viewmodel.ListAuthoredChallengeViewModel
import kotlinx.android.synthetic.main.fragment_list_completed_challenge.*
import javax.inject.Inject

class ListAuthoredChallengeFragment : Fragment(R.layout.fragment_list_authored_challenge), Injectable {

    lateinit var adapter: AuthoredChallengeAdapter

    @Inject
    lateinit var viewModel: ListAuthoredChallengeViewModel

    var args: UserDetailFragmentArgs? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initObservers()
        initrRefresh()
        initRetryButton()
        fetchCompletedChallenges()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = UserDetailFragmentArgs.fromBundle(it)
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
        viewModel.listUserAuthoredChallenge(args?.username)
    }

    private fun initObservers() {
        observe(viewModel.dataAuthoredChallenge, this::onLoadDataCompletedChallenge)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
        observe(viewModel.clean, this::onClean)
        observe(viewModel.retry, this::onRetry)
    }

    private fun initRecycleView() {
        adapter = AuthoredChallengeAdapter()
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
        adapter.itemOnClick = this::onItemClick
    }

    private fun onItemClick(challange: AuthoredChallenge) {
        hideKeyboard()
        (parentFragment as? UserDetailFragment)?.navigateToChallenge(challange.id)
    }

    private fun onLoadDataCompletedChallenge(data: List<AuthoredChallenge>) {
        adapter.submitList(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onRetry(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
        retrybutton.isVisible = true
    }

    private fun onLoading(data: Boolean) {
        swiperefresh.isRefreshing = data && adapter.currentList.isEmpty()
    }

    private fun onClean(data: Boolean) {
        if (data)
            adapter.submitList(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) =
            ListAuthoredChallengeFragment().apply {
                arguments = args
            }
    }
}
