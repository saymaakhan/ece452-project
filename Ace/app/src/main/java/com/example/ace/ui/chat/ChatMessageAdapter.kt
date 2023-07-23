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
<<<<<<< HEAD
<<<<<<< HEAD
                         private val messagesReceived: ArrayList<ChatMessage>,
                         private val messagesSent: ArrayList<ChatMessage>,
                         private val currentUserName: String?,
                         private val otherUser: String?
=======
=======
                         private val messagesReceived: ArrayList<ChatMessage>,
                         private val messagesSent: ArrayList<ChatMessage>,
>>>>>>> 2c0fbb6 (update messaging functionality)
                         private val currentUserName: String?
>>>>>>> 0c9dc5c (load users and save sent messages)
) : FirebaseRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder>(options) {
    private final val SENT_MESSAGE : Int = 1
    private final val RECEIVE_MESSAGE: Int = 2
    private final val NONE: Int = 0
    var sent = 0
    var received = 0

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

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 2c0fbb6 (update messaging functionality)
    override fun getItemViewType(position: Int): Int {
        // Implement your logic to determine whether the message is sent or received
        // For example, you can check if the message sender is the current user
        val message = getItem(position)
<<<<<<< HEAD
        if (message.sender == currentUserName && message.receiver == otherUser) {
            return SENT_MESSAGE
        } else if (message.sender == otherUser && message.receiver == currentUserName){
            return RECEIVE_MESSAGE
        }
        return NONE
    }
=======
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
>>>>>>> 0c9dc5c (load users and save sent messages)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: ChatMessage) {
        val viewType = getItemViewType(position)

<<<<<<< HEAD
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
=======
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
>>>>>>> 0c9dc5c (load users and save sent messages)
=======
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
>>>>>>> 2c0fbb6 (update messaging functionality)

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        open fun bind (message : ChatMessage) {
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    class SentMessageHolder(ItemView: View) : ViewHolder(ItemView) {
=======
    class SentMessageHolder(ItemView: View) : ChatMessageAdapter.ViewHolder(ItemView) {
>>>>>>> 0c9dc5c (load users and save sent messages)
=======
    class SentMessageHolder(ItemView: View) : ViewHolder(ItemView) {
>>>>>>> 2c0fbb6 (update messaging functionality)
        val messageTextView : TextView =  itemView.findViewById(R.id.text_chat_message_self)
        val timeTextView : TextView = itemView.findViewById(R.id.text_chat_timestamp_self)

        override fun bind (message : ChatMessage) {
            messageTextView.text = message.message
<<<<<<< HEAD
<<<<<<< HEAD
            Log.d(TAG, "MessageTextView: ${messageTextView.text}")
=======
>>>>>>> 0c9dc5c (load users and save sent messages)
=======
            Log.d(TAG, "MessageTextView: ${messageTextView.text}")
>>>>>>> 2c0fbb6 (update messaging functionality)
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
<<<<<<< HEAD
<<<<<<< HEAD
            Log.d(TAG, "Received MessageTextView: ${messageTextView.text}")
            val dt = message.timestamp?.let { Date(it) }
=======
            val dt = message.timestamp?.let { Date(it) }
            userTextView.text = ""
>>>>>>> 0c9dc5c (load users and save sent messages)
=======
            Log.d(TAG, "Received MessageTextView: ${messageTextView.text}")
            val dt = message.timestamp?.let { Date(it) }
>>>>>>> 2c0fbb6 (update messaging functionality)
            timeTextView.text = dt.toString()
        }
    }

<<<<<<< HEAD
    class PlaceHolder(ItemView: View) : ViewHolder(ItemView) {

    }

=======
>>>>>>> 0c9dc5c (load users and save sent messages)
}