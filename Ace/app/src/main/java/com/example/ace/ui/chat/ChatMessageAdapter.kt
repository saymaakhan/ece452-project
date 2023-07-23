package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.util.Log
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
                         private val messagesReceived: ArrayList<ChatMessage>,
                         private val messagesSent: ArrayList<ChatMessage>,
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

    override fun getItemViewType(position: Int): Int {
        // Implement your logic to determine whether the message is sent or received
        // For example, you can check if the message sender is the current user
        val message = getItem(position)
        return if (message.sender == currentUserName) {
            SENT_MESSAGE
        } else {
            RECEIVE_MESSAGE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: ChatMessage) {
        val viewType = getItemViewType(position)

        if (viewType == SENT_MESSAGE) {
            val sentPosition = position - messagesReceived.size
            if (sentPosition >= 0 && sentPosition < messagesSent.size) {
                val message = messagesSent[sentPosition]
                val sentHolder = holder as SentMessageHolder
                sentHolder.bind(message)
            }
        } else if (viewType == RECEIVE_MESSAGE) {
            val receivedPosition = position
            if (receivedPosition >= 0 && receivedPosition < messagesReceived.size) {
                val message = messagesReceived[receivedPosition]
                val receivedHolder = holder as ReceivedMessageHolder
                receivedHolder.bind(message)
            }
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
            Log.d(TAG, "MessageTextView: ${messageTextView.text}")
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.text = dt.toString()
        }
    }

    class ReceivedMessageHolder(ItemView: View) : ViewHolder(ItemView) {
//        val userTextView : TextView = itemView.findViewById(R.id.text_chat_user_peer)
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_peer)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_peer)

        override fun bind(message : ChatMessage) {
            messageTextView.text = message.message
            Log.d(TAG, "Received MessageTextView: ${messageTextView.text}")
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.text = dt.toString()
        }
    }

}