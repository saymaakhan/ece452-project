package com.example.ace.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.DiscussionForumTopic
import org.w3c.dom.Text


class DiscussionForumTopicsAdapter(private val mList: MutableList<DiscussionForumTopic>, val clickListner : (DiscussionForumTopic, Int) -> Unit) : RecyclerView.Adapter<DiscussionForumTopicsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forum_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = mList[position]
        holder.bind(topic)

        holder.itemView.setOnClickListener { clickListner(topic,position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val topicPictureImageView : ImageView =  itemView.findViewById(R.id.forum_topic_picture)
        val nameTextView : TextView = itemView.findViewById(R.id.forum_topic_name)
        val numberOfStudentsTextView : TextView = itemView.findViewById(R.id.num_of_users)
        open fun bind (topic : DiscussionForumTopic) {
            nameTextView.setText(topic.topicName)
            numberOfStudentsTextView.setText(topic.numberOfStudents.toString())
            topicPictureImageView.setImageResource(R.drawable.chat_icon)
        }
    }
}
