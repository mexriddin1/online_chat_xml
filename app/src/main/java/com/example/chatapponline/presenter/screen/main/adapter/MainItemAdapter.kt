package com.example.chatapponline.presenter.screen.main.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapponline.R
import com.example.chatapponline.databinding.UserItemBinding
import com.example.chatapponline.sourse.data_class.User

class MainItemAdapter : ListAdapter<User, MainItemAdapter.UserViewHolder>(UserDiffCallback()) {
    private var clickItemListener: ((id: String) -> Unit)? = null

    inner class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickItemListener?.invoke(getItem(adapterPosition).userEmail)
            }
        }

        @SuppressLint("ResourceType")
        fun bind(position: Int) {
            val user = getItem(position)

            Glide.with(binding.root)
                .load(getItem(adapterPosition).userImg)
                .placeholder(R.raw.user)
                .into(binding.userImg)

            binding.userOnline.setBackgroundResource(if (user.userOnline) R.drawable.online_shape else R.drawable.ofline_shape)
            binding.username.text = getItem(position).userName
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    fun setItemClickListener(l: (String) -> Unit) {
        clickItemListener = l
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(position)
}
