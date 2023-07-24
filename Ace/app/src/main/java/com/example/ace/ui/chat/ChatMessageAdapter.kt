package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.ChatMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.util.Date

class ChatMessageAdapter(private val options: FirebaseRecyclerOptions<ChatMessage>,
                         private val messagesReceived: ArrayList<ChatMessage>,
                         private val messagesSent: ArrayList<ChatMessage>,
                         private val currentUserName: String?,
                         private val otherUser: String?
) : FirebaseRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder>(options) {
    private final val SENT_MESSAGE : Int = 1
    private final val RECEIVE_MESSAGE: Int = 2
    private final val NONE: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if( viewType == SENT_MESSAGE && messagesSent.isNotEmpty()) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_self, parent, false)
            return SentMessageHolder(view)
        }
        else if (viewType == RECEIVE_MESSAGE && messagesReceived.isNotEmpty()) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_peer, parent, false)
            return ReceivedMessageHolder(view)
        }
        val view = View(parent.context)
        view.visibility = View.GONE
        return PlaceHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        // Implement your logic to determine whether the message is sent or received
        // For example, you can check if the message sender is the current user
        val message = getItem(position)
        if (message.sender == currentUserName && message.receiver == otherUser) {
            return SENT_MESSAGE
        } else if (message.sender == otherUser && message.receiver == currentUserName){
            return RECEIVE_MESSAGE
        }
        return NONE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: ChatMessage) {
        val viewType = getItemViewType(position)

        if (viewType == SENT_MESSAGE && messagesSent.isNotEmpty()) {
            val message = getItem(position)
            val sentHolder = holder as SentMessageHolder
            sentHolder.bind(message)
        } else if (viewType == RECEIVE_MESSAGE && messagesReceived.isNotEmpty()) {
            val message = getItem(position)
            val receivedHolder = holder as ReceivedMessageHolder
            receivedHolder.bind(message)
        }
    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        open fun bind (message : ChatMessage) {
        }
    }

    class SentMessageHolder(ItemView: View) : ViewHolder(ItemView) {
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_self)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_self)

        override fun bind (message : ChatMessage) {
            messageTextView.text = message.message
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.text = dt.toString()
        }
    }

    class ReceivedMessageHolder(ItemView: View) : ViewHolder(ItemView) {
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_peer)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_peer)

        override fun bind(message : ChatMessage) {
            messageTextView.text = message.message
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.text = dt.toString()
        }
    }

    class PlaceHolder(ItemView: View) : ViewHolder(ItemView) {

    }

}