package br.com.anderson.cocuscodechallenge.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import java.util.concurrent.Executors


class AuthoredChallengeAdapter : ListAdapter<AuthoredChallenge,AuthoredChallengeAdapter.Holder>(
    AsyncDifferConfig.Builder<AuthoredChallenge>(diffCallback)
    .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
    .build()){

    var itemOnClick: (AuthoredChallenge) -> Unit = {_ ->  }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthoredChallengeAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_authored_challenge, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder,getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    private fun binds(holder: Holder, data: AuthoredChallenge) {
        with(holder){
            itemView.setOnClickListener {
                itemOnClick(data)
            }

            languages.text = data.languages.joinToString(", ")
            name.text = data.name
            description.text = data.description
            rank.text = data.rankName
        }


    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val languages: TextView = view.findViewById(R.id.languages)
        val description: TextView = view.findViewById(R.id.description)
        val rank: TextView = view.findViewById(R.id.rank)
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<AuthoredChallenge> =
                object : DiffUtil.ItemCallback<AuthoredChallenge>() {
                    override fun areItemsTheSame(oldItem: AuthoredChallenge, newItem: AuthoredChallenge): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: AuthoredChallenge, newItem: AuthoredChallenge): Boolean {
                        return oldItem == newItem
                    }
                }
    }
}

