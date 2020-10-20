package br.com.anderson.cocuscodechallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.extras.toDateFormat
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import java.util.concurrent.Executors

class CompletedChallengeAdapter : ListAdapter<CompletedChallenge, CompletedChallengeAdapter.Holder>(
    AsyncDifferConfig.Builder<CompletedChallenge>(diffCallback)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    var itemOnClick: (CompletedChallenge) -> Unit = { _ -> }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedChallengeAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_completed_challenge, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder, getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    private fun binds(holder: Holder, data: CompletedChallenge) {
        with(holder) {
            itemView.setOnClickListener {
                itemOnClick(data)
            }
            completedlanguages.text = data.completedLanguages?.joinToString(", ")
            name.text = data.name
            date.text = data.completedAt?.toDateFormat()
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val completedlanguages: TextView = view.findViewById(R.id.completedlanguages)
        val date: TextView = view.findViewById(R.id.date)
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<CompletedChallenge> =
            object : DiffUtil.ItemCallback<CompletedChallenge>() {
                override fun areItemsTheSame(oldItem: CompletedChallenge, newItem: CompletedChallenge): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: CompletedChallenge, newItem: CompletedChallenge): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
