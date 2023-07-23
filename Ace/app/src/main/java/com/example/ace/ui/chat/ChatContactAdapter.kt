package com.example.ace.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.data.model.ChatUser

class ChatContactAdapter(private val mList: MutableList<String>, val clickListener : (String, Int) -> Unit) : RecyclerView.Adapter<ChatContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mList[position]
        holder.bind(user)

        holder.itemView.setOnClickListener { clickListener(user,position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val profilePicImage : ImageView =  itemView.findViewById(R.id.profile_picture)
        private val contactNameText : TextView = itemView.findViewById(R.id.contact_name)
        open fun bind (user : String) {
            contactNameText.setText(user)
            profilePicImage.setImageResource(R.drawable.sharp_person_24)
        }
    }
}