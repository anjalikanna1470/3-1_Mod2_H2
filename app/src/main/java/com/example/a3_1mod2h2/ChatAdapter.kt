package com.example.a3_1mod2h2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private val messages: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object {
        private const val USER = 1
        private const val ASSISTANT = 2
        private const val ERROR = 3
    }

    override fun getItemViewType(position: Int): Int {

        val message = messages[position]

        return when {
            message.isError -> ERROR
            message.isUser -> USER
            else -> ASSISTANT
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {

        val layout = when (viewType) {
            USER -> R.layout.item_message_user
            ASSISTANT -> R.layout.item_message_assistant
            else -> R.layout.item_message_error
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int
    ) {
        holder.textView.text = messages[position].message
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.lastIndex)
    }

    class ChatViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView =
            itemView.findViewById(R.id.tvMessage)
    }
}