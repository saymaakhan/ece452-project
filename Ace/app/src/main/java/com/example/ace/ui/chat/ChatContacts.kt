package com.example.ace.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

import com.example.ace.databinding.ActivityChatContactsBinding
import com.example.ace.data.model.ChatUser

class ChatContacts : AppCompatActivity() {
    private lateinit var binding: ActivityChatContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // -- Create a list of contacts
        val users : MutableList<ChatUser> = mutableListOf()
        val user1 = ChatUser(userName = "John", profileUrl = "")
        val user2 = ChatUser(userName = "Jane", profileUrl = "")
        val user3 = ChatUser(userName = "Tim", profileUrl = "")
        users.add(user1)
        users.add(user2)
        users.add(user3)

        // -- Set the recyclerview and initialize it with adapter
        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_contacts)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val adapter = ChatContactAdapter(users){user:ChatUser, position: Int ->
            val userName = user.userName
            val intent = Intent(this, SingleChat::class.java)
            intent.putExtra("ChatUser", userName )
            startActivity(intent)
            finish()
        }
        recyclerview.adapter = adapter
    }
}
