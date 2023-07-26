package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.example.ace.R

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ace.MainActivity

import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.ChatUser
import com.example.ace.data.DiscussionForumMessageSource
import com.example.ace.data.model.DiscussionMessage

import com.example.ace.databinding.ActivityDiscussionForumChatBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp


class DiscussionForumChat : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionForumChatBinding
    private var responseIndex : Int = 0
    private var topicName : String = ""
    private lateinit var manager: LinearLayoutManager
    private var adapter: DiscussionForumChatMessageAdapter? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var messagesReceivedList: ArrayList<DiscussionMessage>
    private lateinit var messagesSentList: ArrayList<DiscussionMessage>

    val badWords = arrayOf("fuck", "shit", "bitch", "ass", "sex", "dick", "cunt", "pussy", "whore", "slut")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_discussion_thread_chat)
        binding = ActivityDiscussionForumChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoPictureView : ImageView =  findViewById(R.id.logo_icon)
        logoPictureView.setImageResource(R.drawable.loginlogo)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        db = Firebase.database
        val discRef = db.reference.child(DISCUSSION_CHILD)
        messagesReceivedList = ArrayList()
        messagesSentList = ArrayList()

        val options = FirebaseRecyclerOptions.Builder<DiscussionMessage>()
            .setQuery(discRef, DiscussionMessage::class.java)
            .build()
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        val topic = intent.getSerializableExtra("topic" ) as String
        topicName = topic

        getUserDiscussionMessages(discRef, topicName)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_space)
        recyclerview.layoutManager=LinearLayoutManager(this)
        adapter = DiscussionForumChatMessageAdapter(options, messagesReceivedList,
            messagesSentList, getUserName(), topicName)
        recyclerview.adapter = adapter

        adapter!!.registerAdapterDataObserver(
            ScrollToBottomDiscussion(binding.recyclerDiscussionForumSpace, adapter!!, manager)
        )

        val channelName  : TextView = findViewById(R.id.discussion_name)
        channelName.text = topicName

        val sendButton : View = findViewById(R.id.button_discussion_forum_send)
        sendButton.setOnClickListener {
            discussionChatSendOnClick()
        }

        val closeButton : View = findViewById(R.id.button_discussion_forum_close)
        closeButton.setOnClickListener {
            val intent = Intent(this, DiscussionForumTopics::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun discussionChatSendOnClick() {
        var text = binding.editDiscussionForumMessage.text.toString()
        if (badWords.any {word -> text.contains(word)}) {
            text = "This message contains profanity"
        }
        val timeStamp = Timestamp(System.currentTimeMillis()).time
        val user = getUserName() as String
        val newMessage = DiscussionMessage(course=topicName, sender=user, timestamp=timeStamp, message=text)
        db.reference.child(DISCUSSION_CHILD).push().setValue(newMessage)
        binding.editDiscussionForumMessage.text.clear()

        // Send chat sent notif
//        val toast = Toast.makeText(this, "Message delivered", Toast.LENGTH_SHORT)
//        toast.setGravity(Gravity.TOP, 0, 0)
//        toast.show()
    }

    private fun getUserDiscussionMessages(dbref : DatabaseReference, course : String) {
        val user = getUserName()
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate through all the children of the "data" node
                messagesSentList.clear()
                messagesReceivedList.clear()
                for (childSnapshot in dataSnapshot.children) {
                    val child = childSnapshot.getValue(DiscussionMessage::class.java)
                    // Access the data for each child
                    if (child != null) {
                        if (child.course == course) {
                            if (child.sender == user) {
                                messagesSentList.add(child)
                            } else {
                                messagesReceivedList.add(child)
                            }
                        }
                    }
                }
                Log.d(TAG, "Received: ${messagesReceivedList.toString()}")
                Log.d(TAG, "Sent: ${messagesSentList.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else SingleChat.ANONYMOUS
    }

    public override fun onPause() {
        adapter?.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter?.startListening()
    }

    companion object {
        const val DISCUSSION_CHILD = "discussion"
        const val ANONYMOUS = "anonymous"
    }
}
