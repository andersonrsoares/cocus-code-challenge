package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.extras.observe
import br.com.anderson.cocuscodechallenge.extras.toDateFormat
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.viewmodel.ChallengeViewModel
import kotlinx.android.synthetic.main.fragment_challange.*
import kotlinx.android.synthetic.main.fragment_list_user.*
import kotlinx.android.synthetic.main.fragment_list_user.progressloading
import javax.inject.Inject

class ChallengeFragment : Fragment(R.layout.fragment_challange), Injectable {

    @Inject
    lateinit var viewModel: ChallengeViewModel

    var args: ChallengeFragmentArgs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = ChallengeFragmentArgs.fromBundle(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        fetchChallenge()
    }

    private fun initObservers() {
        observe(viewModel.dataChallenge, this::onLoadChallenge)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
    }

    private fun fetchChallenge() {
        viewModel.listChallenge(args?.id)
    }

    private fun onLoadChallenge(data: Challenge) {
        description.text = data.description
        name.text = data.name
        rank.text = data.rank?.name
        languages.text = data.languages?.joinToString(", ")
        tags.text = data.tags?.joinToString(", ")
        createdby.text = data.createdBy?.username
        createdat.text = data.publishedAt?.toDateFormat()
        category.text = data.category
        starts.text = data.totalStars.toString()
        attempts.text = data.totalAttempts.toString()
        attempts.text = data.totalCompleted.toString()
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        progressloading.isVisible = data
    }
}
