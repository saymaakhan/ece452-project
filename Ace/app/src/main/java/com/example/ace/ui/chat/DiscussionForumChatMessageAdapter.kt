package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.DiscussionMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.util.Date

class DiscussionForumChatMessageAdapter(private val options: FirebaseRecyclerOptions<DiscussionMessage>,
                                        private val messagesReceived: ArrayList<DiscussionMessage>,
                                        private val messagesSent: ArrayList<DiscussionMessage>,
                                        private val currentUserName: String?,
                                        private val course: String
) : FirebaseRecyclerAdapter<DiscussionMessage, RecyclerView.ViewHolder>(options) {
    private final val NONE : Int = 0
    private final val SENT_MESSAGE : Int = 1
    private final val RECEIVE_MESSAGE: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == SENT_MESSAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion_forum_self, parent, false)
            return SentMessageHolder(view)
        }
        else if(viewType == RECEIVE_MESSAGE){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion_forum_peers, parent, false)
            return ReceivedMessageHolder(view)
        }
        val view = View(parent.context)
        view.visibility = View.GONE
        return DiscussionPlaceHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: DiscussionMessage
    ) {
        val message = getItem(position)

        if (holder.getItemViewType() == SENT_MESSAGE) {
            val h : SentMessageHolder = holder as SentMessageHolder
            h.bind(message, 1)
        }
        else if (holder.getItemViewType() == RECEIVE_MESSAGE){
            val h : ReceivedMessageHolder = holder as ReceivedMessageHolder
            h.bind(message,2)
        }
//        holder.itemView.visibility = if (message.course == course) View.VISIBLE else View.GONE
    }

    override fun getItemViewType(position: Int) : Int {
        val message = getItem(position)
        if (message.course != course) return NONE
        return if (message.sender == currentUserName) {
            SENT_MESSAGE
        } else {
            RECEIVE_MESSAGE
        }
    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        open fun bind (message : DiscussionMessage, type: Int) {
        }
    }

    class SentMessageHolder(ItemView: View) : ViewHolder(ItemView) {
        val messageTextView : TextView =  itemView.findViewById(R.id.text_forum_message_self)
        val timeTextView : TextView = itemView.findViewById(R.id.text_forum_timestamp_self)

        override fun bind(message: DiscussionMessage, type: Int) {
            messageTextView.setText(message.message)
            val dt = message.timestamp?.let { Date(it) }
            timeTextView.setText(dt.toString())
        }
    }

    class ReceivedMessageHolder(ItemView: View) : ViewHolder(ItemView) {
        val userTextView : TextView = itemView.findViewById(R.id.text_forum_user_peer)
        val messageTextView : TextView =  itemView.findViewById(R.id.text_forum_message_peer)
        val timeTextView : TextView = itemView.findViewById(R.id.text_forum_timestamp_peer)
        val peerPictureImageView : ImageView =  itemView.findViewById(R.id.profile_picture)

        override fun bind(message: DiscussionMessage, type: Int) {
            if (type == 2) {
                messageTextView.text = message.message
                val dt = message.timestamp?.let { Date(it) }
                userTextView.text = message.sender
                timeTextView.text = dt.toString()
                peerPictureImageView.setImageResource(R.drawable.face_48px)
            }
        }
    }
    class DiscussionPlaceHolder(ItemView: View) : ViewHolder(ItemView) {

    }
}
