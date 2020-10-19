package br.com.anderson.cocuscodechallenge.ui.listuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.ListUserAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.hideKeyboard
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.ui.ListUserFragmentDirections
import kotlinx.android.synthetic.main.fragment_list_user.*
import javax.inject.Inject

class ListUserFragment : Fragment(R.layout.fragment_list_user), Injectable, SearchView.OnQueryTextListener {

    lateinit var adapter: ListUserAdapter

    lateinit var viewModel: ListUserViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(ListUserViewModel::class.java)
        init()
    }

    fun init() {
        initSearch()
        initObservers()
        initRecycleView()
        initMessageEmpty()
        loadUsers()
    }

    private fun initMessageEmpty() {
        onEmpty(false)
    }

    private fun initSearch() {
        searchview.setOnQueryTextListener(this)
    }

    private fun loadUsers() {
        viewModel.listLastUsers()
    }

    private fun initObservers() {
        observe(viewModel.dataListLastUsers, this::onLoadDataListUsers)
        observe(viewModel.newUser, this::onNewUser)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
        observe(viewModel.retry, this::onRetry)
        observe(viewModel.empty, this::onEmpty)
        observe(viewModel.clean, this::onClean)
    }

    private fun initListAdapter() {
        adapter = ListUserAdapter()
        adapter.itemOnClick = this::onItemClick
    }

    private fun initRecycleView() {
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
    }

    private fun onItemClick(user: User) {
        hideKeyboard()
        navController().navigate(
            ListUserFragmentDirections.actionListUserFragmentToUserDetailFragment(
                user.username
            )
        )
    }

    private fun onLoadDataListUsers(data: List<User>) {
        adapter.submitList(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onRetry(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        progressloading.isVisible = data
    }

    private fun onEmpty(data: Boolean) {
        messageempty.isVisible = data
    }

    private fun onClean(data: Boolean) {
        if (data)
            adapter.submitList(arrayListOf())
    }

    private fun onNewUser(data: Boolean) {
        if (data)
            recycleview.scrollToPosition(0)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.searchUser(query ?: "")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.order_rank -> viewModel.orderByPosition()
            else -> viewModel.orderByLookUp()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
