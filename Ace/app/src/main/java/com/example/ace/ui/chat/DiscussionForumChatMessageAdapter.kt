package com.example.ace.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.DiscussionMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.util.Date

class DiscussionForumChatMessageAdapter(private val options: FirebaseRecyclerOptions<DiscussionMessage>,
                                        private val messagesReceived: ArrayList<ChatMessage>,
                                        private val messagesSent: ArrayList<ChatMessage>,
                                        private val currentUserName: String?,
                                        private val otherUser: String?
) : FirebaseRecyclerAdapter<DiscussionMessage, RecyclerView.ViewHolder>(options) {
    private final val NONE : Int = 0
    private final val SENT_MESSAGE : Int = 1
    private final val RECEIVE_MESSAGE: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == SENT_MESSAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion_forum_self, parent, false)
            return SentMessageHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion_forum_peers, parent, false)
            return ReceivedMessageHolder(view)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: DiscussionMessage
    ) {
        val message = getItem(position)

        if (holder.getItemViewType() == SENT_MESSAGE) {
            val h : SentMessageHolder = holder as SentMessageHolder
//            h.bind(message)
        }
        else {
            val h : ReceivedMessageHolder = holder as ReceivedMessageHolder
//            h.bind(message)
        }
    }

//    fun addMessage(msg:ChatMessage) {
//        mList.add(msg)
//    }

    override fun getItemViewType(position: Int) : Int {
        val message = getItem(position)
        val users =
//        if (message.message.sender == currentUserName && message.receiver == otherUser) {
//            return SENT_MESSAGE
//        } else if (message.sender == otherUser && message.receiver == currentUserName){
//            return RECEIVE_MESSAGE
//        }
        return NONE
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }

    override fun getItemCount(): Int {
//        return mList.size
        return 0
    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        open fun bind (message : ChatMessage) {
        }
    }

    class SentMessageHolder(ItemView: View) : DiscussionForumChatMessageAdapter.ViewHolder(ItemView) {
        val messageTextView : TextView =  itemView.findViewById(R.id.text_forum_message_self)
        val timeTextView : TextView = itemView.findViewById(R.id.text_forum_timestamp_self)

        override fun bind (message : ChatMessage) {
            messageTextView.setText(message.message)
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.setText(dt.toString())
        }
    }

    class ReceivedMessageHolder(ItemView: View) : DiscussionForumChatMessageAdapter.ViewHolder(ItemView) {
        val userTextView : TextView = itemView.findViewById(R.id.text_forum_user_peer)
        val messageTextView : TextView =  itemView.findViewById(R.id.text_forum_message_peer)
        val timeTextView : TextView = itemView.findViewById(R.id.text_forum_timestamp_peer)
        val peerPictureImageView : ImageView =  itemView.findViewById(R.id.profile_picture)

        override fun bind(message : ChatMessage) {
            messageTextView.setText(message.message)
            val dt = message.timestamp?.let { Date(it) }
            userTextView.setText(message.sender)
            timeTextView.setText(dt.toString())
            peerPictureImageView.setImageResource(R.drawable.face_48px)
        }
    }
}
