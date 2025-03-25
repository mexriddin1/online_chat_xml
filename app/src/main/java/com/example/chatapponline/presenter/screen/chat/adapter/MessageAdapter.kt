package com.example.chatapponline.presenter.screen.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//
import com.example.chatapponline.R
import com.example.chatapponline.sourse.data_class.Message
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("UNREACHABLE_CODE")
class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(DU) {
    private val localStorage: LocalStorage = LocalStorage.instance

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    object DU : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    private inner class SenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.message)
        private val messageTime: TextView = view.findViewById(R.id.clock_text)

        fun bind(message: Message) {
            messageText.text = message.message
            val date = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date(getItem(adapterPosition).messageTime))
            messageTime.text = date
        }
    }


    private inner class ReaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.message)
        private val messageTime: TextView = view.findViewById(R.id.clock_text)

        fun bind(message: Message) {
            val date = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date(getItem(adapterPosition).messageTime))
            messageText.text = message.message
            messageTime.text = date

        }
    }


    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)

        val message = getItem(position)
        Log.d("ZZZ", "getItemViewType: ${localStorage.getUserEmailId()}= ${message.senderId} ")
        return if (message.senderId == localStorage.getUserEmailId()) VIEW_TYPE_SENDER else VIEW_TYPE_RECEIVER
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENDER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sender_message, parent, false)
            SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reader_message, parent, false)
            ReaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SenderViewHolder) {
            holder.bind(message)
        } else if (holder is ReaderViewHolder) {
            holder.bind(message)
        }
    }
}
