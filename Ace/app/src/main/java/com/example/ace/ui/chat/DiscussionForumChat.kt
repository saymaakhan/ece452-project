package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ace.R

import android.view.View
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var messagesReceivedList: ArrayList<DiscussionMessage>
    private lateinit var messagesSentList: ArrayList<DiscussionMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_discussion_thread_chat)
        binding = ActivityDiscussionForumChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(discRef, ChatMessage::class.java)
            .build()
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        val topic = intent.getSerializableExtra("topic" ) as String
        topicName = topic

        getUserDiscussionMessages(discRef, topicName)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_space)
        recyclerview.layoutManager=LinearLayoutManager(this)
//        val adapter = DiscussionForumChatMessageAdapter(startupMessages)
//        recyclerview.adapter = adapter
//
//        adapter.registerAdapterDataObserver(
//            ScrollToBottom(binding.recyclerChatSpace, adapter, manager)
//        )

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
        val user = getUserName() as String

        val receivers_uid = ArrayList<String>()
        val receivers = ArrayList<String>()
        val firestore = FirebaseFirestore.getInstance()
        val userCourses = firestore.collection("user_enrolled_classes")

        userCourses.document(topicName).collection("users").get()
            .addOnSuccessListener { querySnapshot ->
                for (userResult in querySnapshot) {
                    if (userResult.exists()) {
                        val uid = userResult.getString("uid")
                        uid?.let { receivers_uid.add(it) }
                    }
                }

                // References: https://stackoverflow.com/questions/62835609/which-is-more-preferred-whenallsuccess-or-a-batch-in-firestore
                // https://stackoverflow.com/questions/51892766/android-firestore-convert-array-of-document-references-to-listpojo/51897261
                val userRef = firestore.collection("users")
                val tasks: MutableList<Task<DocumentSnapshot>> = mutableListOf()

                for (receiver in receivers_uid) {
                    val task = userRef.document(receiver).get()
                    tasks.add(task)
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener { snapshots ->
                        for (snapshot in snapshots) {
                            if (snapshot.exists()) {
                                val name = snapshot.getString("displayName")
                                if (name != user) {
                                    name?.let {
                                        Log.d(TAG, "User: $it")
                                        receivers.add(it)
                                    }
                                }
                            }
                        }

                        Log.d(TAG, "Receivers: ${receivers.toString()}")
                        val newMessage = DiscussionMessage(course=topicName, sender=user, timestamp=timeStamp, message=text, users =receivers)
                        db.reference.child(DISCUSSION_CHILD).push().setValue(newMessage)
                        Log.d(TAG,"done")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error getting users: ", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting users' UIDs: ", exception)
            }
        binding.editDiscussionForumMessage.text.clear()
    }

    private fun getUserDiscussionMessages(dbref : DatabaseReference, course : String) {

    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else SingleChat.ANONYMOUS
    }

    companion object {
        const val DISCUSSION_CHILD = "discussion"
        const val ANONYMOUS = "anonymous"
    }
}
