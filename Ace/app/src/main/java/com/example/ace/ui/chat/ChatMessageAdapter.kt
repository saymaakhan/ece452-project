package com.example.ace.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.ChatMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.util.Date

class ChatMessageAdapter(private val options: FirebaseRecyclerOptions<ChatMessage>,
                         private val currentUserName: String?
) : FirebaseRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder>(options) {
    private final val SENT_MESSAGE : Int = 1
    private final val RECEIVE_MESSAGE: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if( viewType == SENT_MESSAGE ) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_self, parent, false)
            return SentMessageHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_peer, parent, false)
            return ReceivedMessageHolder(view)
        }
    }

//    override fun getItemViewType(position: Int) : Int {
//        val message : ChatMessage = mList[position]
//
//        if (message.sender.userName.equals("self") ) {
//            return SENT_MESSAGE
//        }
//        else {
//            return RECEIVE_MESSAGE
//        }
//    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: ChatMessage) {
//        val message = mList[position]

        if (holder.getItemViewType() == SENT_MESSAGE ) {
            val h : SentMessageHolder = holder as SentMessageHolder
            h.bind(model)
        }
        else {
            val h : ReceivedMessageHolder = holder as ReceivedMessageHolder
            h.bind(model)
        }
    }

//    override fun getItemCount(): Int {
//        return mList.size
//    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        open fun bind (message : ChatMessage) {
        }
    }

    class SentMessageHolder(ItemView: View) : ChatMessageAdapter.ViewHolder(ItemView) {
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_self)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_self)

        override fun bind (message : ChatMessage) {
            messageTextView.text = message.message
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.text = dt.toString()
        }
    }

    class ReceivedMessageHolder(ItemView: View) : ChatMessageAdapter.ViewHolder(ItemView) {
        val userTextView : TextView = itemView.findViewById(R.id.text_chat_user_peer)
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_peer)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_peer)

        override fun bind(message : ChatMessage) {
            messageTextView.text = message.message
            val dt = message.timestamp?.let { Date(it) }
            userTextView.text = ""
            timeTextView.text = dt.toString()
        }
    }

}
