package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.adapter.ListUserAdapter
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.setDivider
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.fragment_list_user.*
import javax.inject.Inject


class ListUserFragment : Fragment(R.layout.fragment_list_user), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter:ListUserAdapter

    val viewModel: ListUserViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initObservers()
        loadUsers()
    }

    private fun loadUsers(){
        viewModel.listLastUsers()
    }

    private fun initObservers(){
        observe(viewModel.dataListLastUsers,this::onLoadDataListUsers)
        observe(viewModel.message,this::onMessage)
    }

    private fun initRecycleView(){
        adapter = ListUserAdapter()
        recycleview.adapter = adapter
        recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recycleview.setDivider(R.drawable.divider_recycleview)
    }

    private fun onLoadDataListUsers(data: List<User>) {
        adapter.submitList(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(),data, Toast.LENGTH_LONG).show()
    }

}