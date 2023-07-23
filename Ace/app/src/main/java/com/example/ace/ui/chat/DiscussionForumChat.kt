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
import com.example.ace.data.DiscussionForumMessageSource

import com.example.ace.databinding.ActivityDiscussionForumChatBinding
import java.sql.Timestamp


class DiscussionForumChat : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionForumChatBinding
    private var responseIndex : Int = 0
    private var topicName : String = "ECE452: Question about exam?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_discussion_thread_chat)
        binding = ActivityDiscussionForumChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topic = intent.getSerializableExtra("topic" ) as String
        topicName = topic

        val messageSource = DiscussionForumMessageSource()
        val startupMessages = messageSource.getMessages(topicName)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_space)
        recyclerview.layoutManager=LinearLayoutManager(this)
        val adapter = DiscussionForumChatMessageAdapter(startupMessages)
        recyclerview.adapter = adapter

        val channelName  = findViewById<Toolbar>(R.id.toolbar_discussion_forum_channel)
        channelName.title = topicName

        val sendButton : View = findViewById(R.id.button_discussion_forum_send)
        sendButton.setOnClickListener {
            discussionChatSendOnClick()
        }

        val closeButton : View = findViewById(R.id.button_discussion_forum_close)
        closeButton.setOnClickListener {
            finish()
        }
    }

    fun discussionChatSendOnClick() {
        val text = binding.editDiscussionForumMessage.text.toString()
        val timeStamp = Timestamp(System.currentTimeMillis()).time
        val user = ChatUser(userName = "self", profileUrl = "")
<<<<<<< HEAD
        val newMessage = ChatMessage(sender="self", timestamp=timeStamp, message=text, receiver="")
=======
        val newMessage = ChatMessage(topic="", sender=user, timestamp=timeStamp, message=text)
>>>>>>> a0f94ec (Forum activity finished and modified chat layout)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_space)
        val adapter = recyclerview.adapter as DiscussionForumChatMessageAdapter
        val index = adapter.itemCount
        adapter.addMessage(newMessage)

        val messageSource = DiscussionForumMessageSource()
        val responseMessage1 = messageSource.getResponseMessage(responseIndex, topicName,"John")
        adapter.addMessage(responseMessage1)
        responseIndex = responseIndex + 1

        val responseMessage2 = messageSource.getResponseMessage(responseIndex, topicName,"Anna")
        adapter.addMessage(responseMessage2)
        responseIndex = responseIndex + 1

        adapter.notifyItemRangeChanged(index, 3)

        binding.editDiscussionForumMessage.text.clear()
    }
}
