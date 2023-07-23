//Reference: https://firebase.google.com/codelabs/firebase-android#6

package com.example.ace.ui.chat
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ace.R
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ace.MainActivity
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.ChatUser
import com.example.ace.data.ChatMessageSource
import com.example.ace.databinding.ActivitySingleChatBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database

import java.sql.Timestamp

class SingleChat : AppCompatActivity() {
    private lateinit var binding: ActivitySingleChatBinding
    private lateinit var manager: LinearLayoutManager
    private var responseIndex : Int = 0
    private var peerName : String = "John Smith"
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: ChatMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Initialize Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(messagesRef, ChatMessage::class.java)
            .build()
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        adapter = ChatMessageAdapter(options, getUserName());
//        binding.messageRecyclerView.layoutManager = manager
//        binding.messageRecyclerView.adapter = adapter

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver for details
//        adapter.registerAdapterDataObserver(
//            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
//        )

        val userName = intent.getSerializableExtra("ChatUser" ) as String
        peerName = userName

        val chatMessageSrc = ChatMessageSource()
        val startupMessages = chatMessageSrc.getMessages("", peerName )

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_space)
        recyclerview.layoutManager=LinearLayoutManager(this)
        adapter = ChatMessageAdapter(options, getUserName());
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
        val user = getUserName() as String
        val newMessage = ChatMessage(sender=user, timestamp=timeStamp, message=textView)

//        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_space)
//        val adapter = recyclerview.adapter as ChatMessageAdapter
//        val index = adapter.itemCount
//        adapter.addMessage(newMessage)

//        val messageSrc = ChatMessageSource()
//        val responseMessage = messageSrc.getResponseMessage(responseIndex, peerName)
//        adapter.addMessage(responseMessage)

//        adapter.notifyItemRangeChanged(index, 2)
//        responseIndex = responseIndex + 1
        db.reference.child(MESSAGES_CHILD).push().setValue(newMessage)
        binding.editChatMessage.text.clear()
    }
    
    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
    }
}