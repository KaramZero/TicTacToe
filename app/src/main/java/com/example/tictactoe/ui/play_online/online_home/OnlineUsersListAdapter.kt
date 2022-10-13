package com.example.tictactoe.ui.play_online.online_home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tictactoe.R
import com.example.tictactoe.databinding.OnlineUsersListRowItemBinding
import com.example.tictactoe.model.User

class OnlineUsersListAdapter constructor(
    private val context: Context,
    private val communicator: OnlineFragmentCommunicator
) :
    ListAdapter<User, OnlineUsersListAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OnlineUsersListRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            if (currentItem.onlineStatus!!)
                currentItem.id?.let { it1 -> communicator.sendInvitation(it1) }
        }
        holder.bind(user = currentItem, context = context)
    }


    fun setData(userArrayList: ArrayList<User>) {
        this.submitList(userArrayList)
    }

    class ViewHolder(private val binding: OnlineUsersListRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, context: Context) {
            binding.apply {
                userNameTextView.text = user.name
                Glide.with(context)
                    .load(user.photoUrl)
                    .into(userImageView)

                val onlineStateImageResource = if (user.onlineStatus!!) R.drawable.online_dot
                else R.drawable.offline_dot

                Glide.with(context)
                    .load(onlineStateImageResource)
                    .into(onlineStateImageView)

            }
        }
    }

}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}
