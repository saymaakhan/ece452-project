package com.example.ace.ui.chat

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ace.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.databinding.ActivityDiscussionForumTopicsBinding
import com.example.ace.data.model.DiscussionForumTopic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class DiscussionForumTopics : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionForumTopicsBinding
    private lateinit var auth: FirebaseAuth

    var topics : MutableList<DiscussionForumTopic> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiscussionForumTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        loadCoursesFromFirestore()

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_topics)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val adapter = DiscussionForumTopicsAdapter(topics){ topic: DiscussionForumTopic, position: Int ->
            val topicName = topic.topicName
            val intent = Intent(this, DiscussionForumChat::class.java)
            intent.putExtra("topic", topicName )
            startActivity(intent)
            finish()
        }
        recyclerview.adapter = adapter
    }
    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else SingleChat.ANONYMOUS
    }
    private fun loadCoursesFromFirestore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_topics)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val currUser = getUserName()
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("user_enrolled_classes")
                .get()
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG,"SUCCESSFUL: ${task.result.size()}")
                    for (document in task.result) {
                        topics.add(DiscussionForumTopic(document.id))
                        Log.d(TAG, "Discussion: $document")
                        Log.d(TAG,"SUCCESSFUL discussion")
                    }
                    val adapter = DiscussionForumTopicsAdapter(topics){ topic: DiscussionForumTopic, position: Int ->
                        val topicName = topic.topicName
                        val intent = Intent(this, DiscussionForumChat::class.java)
                        intent.putExtra("topic", topicName )
                        startActivity(intent)
                        finish()
                    }
                    recyclerview.adapter = adapter
                    Log.d(ContentValues.TAG, topics.toString())
                }

                else {
                    Log.d(ContentValues.TAG, "Error getting documents: ", task.exception)
                }
            }
        }
    }
}
