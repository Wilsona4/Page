package com.example.page.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.page.databinding.UserItemBinding
import com.example.page.domain.model.User

class UserPagingDataAdapter(private val interaction: Interaction? = null) :
    PagingDataAdapter<User, UserPagingDataAdapter.PagingViewHolder>(diffCallback) {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagingViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
//        (holder as? PagingViewHolder)?.bind(differ.currentList[position])
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
        }
    }

    class PagingViewHolder constructor(
        private var binding: UserItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(user)
            }

            binding.favourite.setOnCheckedChangeListener(null)

            binding.apply {
                login.text = user.login
                url.text = user.html_url
                profileImage.load(user.avatar_url)
                favourite.isChecked = user.isFavourite
                favourite.setOnCheckedChangeListener { _, checked ->
                    interaction?.updateCheck(user, checked)
                }
            }

        }
    }

    interface Interaction {
        fun onItemSelected(item: User)
        fun updateCheck(item: User, isChecked: Boolean)
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}