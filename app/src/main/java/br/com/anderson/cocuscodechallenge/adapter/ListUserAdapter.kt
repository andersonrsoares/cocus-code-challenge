package br.com.anderson.cocuscodechallenge.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.model.User
import java.util.concurrent.Executors


class ListUserAdapter : ListAdapter<User,ListUserAdapter.Holder>(
    AsyncDifferConfig.Builder<User>(diffCallback)
    .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
    .build()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_user, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder,getItem(position)!!,position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    private fun binds(holder: Holder,data: User,pos: Int) {
        with(holder){
            itemView.setOnClickListener {
                notifyItemChanged(pos)
            }

            username .text = data.username
            name.text = data.clan
            honor.text = data.honor.toString()
            position.text = data.leaderboardPosition.toString()
            bestlanguage.text = data.bestLanguageAndPoints()
        }


    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.username)
        val name: TextView = view.findViewById(R.id.name)
        val position: TextView = view.findViewById(R.id.position)
        val honor: TextView = view.findViewById(R.id.honor)
        val bestlanguage: TextView = view.findViewById(R.id.bestlanguage)
    }

    fun insert(user: User){
       submitList(currentList.toMutableList().apply {
           add(0,user)
       })
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<User> =
                object : DiffUtil.ItemCallback<User>() {
                    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                        return oldItem == newItem
                    }
                }
    }
}

