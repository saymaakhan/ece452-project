package com.example.ace.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ace.R

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.ChatUser
import com.example.ace.data.ChatMessageSource

import com.example.ace.databinding.ActivitySingleChatBinding
import java.sql.Timestamp


class SingleChat : AppCompatActivity() {
    private lateinit var binding: ActivitySingleChatBinding
    private var responseIndex : Int = 0
    private var peerName : String = "John Smith"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getSerializableExtra("ChatUser" ) as String
        peerName = userName

        val chatMessageSrc = ChatMessageSource()
        val startupMessages = chatMessageSrc.getMessages("", peerName )

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_space)
        recyclerview.layoutManager=LinearLayoutManager(this)
        val adapter = ChatMessageAdapter(startupMessages)
        recyclerview.adapter = adapter

        val directMessageUserName  = findViewById<Toolbar>(R.id.toolbar_chatchannel)
        directMessageUserName.title = peerName

        val sendButton : View = findViewById(R.id.button_chat_send)
        sendButton.setOnClickListener {
            chatSendOnClick()
        }

        val closeButton : View = findViewById(R.id.button_chat_close)
        closeButton.setOnClickListener {
            finish()
        }
    }

    fun chatSendOnClick() {
        val textView = binding.editChatMessage.text.toString()
        val timeStamp = Timestamp(System.currentTimeMillis()).time
        val user = ChatUser(userName = "self", profileUrl = "")
        val newMessage = ChatMessage(topic="", sender=user, timestamp=timeStamp, message=textView)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_space)
        val adapter = recyclerview.adapter as ChatMessageAdapter
        val index = adapter.itemCount
        adapter.addMessage(newMessage)

        val messageSrc = ChatMessageSource()
        val responseMessage = messageSrc.getResponseMessage(responseIndex, peerName)
        adapter.addMessage(responseMessage)

        adapter.notifyItemRangeChanged(index, 2)
        responseIndex = responseIndex + 1

        binding.editChatMessage.text.clear()
    }
}
