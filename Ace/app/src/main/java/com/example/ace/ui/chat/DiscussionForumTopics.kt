package com.example.ace.ui.chat

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.compose.ui.text.toLowerCase
import com.example.ace.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.databinding.ActivityDiscussionForumTopicsBinding
import com.example.ace.data.model.DiscussionForumTopic
import com.example.ace.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class DiscussionForumTopics : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionForumTopicsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionForumTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoPictureView : ImageView =  findViewById(R.id.logo_icon)
        logoPictureView.setImageResource(R.drawable.loginlogo)
        val iconPictureView : ImageView =  findViewById(R.id.icon_account_picture)
        iconPictureView.setImageResource(R.drawable.account_circle_48px__1_)

        auth = Firebase.auth
        loadCoursesFromFirestore()

        iconPictureView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun getUserName(): String? {
        val user = auth.currentUser
        return user?.uid ?: SingleChat.ANONYMOUS
    }
    private fun loadCoursesFromFirestore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_topics)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val currUser = getUserName()
        Log.d(TAG, "curr user: $currUser")
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            var numberOfStudents = 0
            val classes = firestore.collection("user_enrolled_classes")
            classes
                .get()
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var topics : MutableList<DiscussionForumTopic> = mutableListOf()
                    for (document in task.result) {
                        val docID = document.id
                        val users = classes.document(docID).collection("users")
                        users.get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (user in it.result) {
                                    numberOfStudents += 1
                                }
                                for (user in it.result) {
                                    val userID = user.data["uid"].toString()
                                    if (userID == currUser) {
                                        topics.add(DiscussionForumTopic(docID, numberOfStudents))
                                    }
                                }
                            }
                            numberOfStudents = 0
                            val adapter =
                                DiscussionForumTopicsAdapter(topics) { topic: DiscussionForumTopic, position: Int ->
                                    val topicName = topic.topicName
                                    val intent = Intent(this, DiscussionForumChat::class.java)
                                    intent.putExtra("topic", topicName)
                                    startActivity(intent)
                                    finish()
                                }
                            recyclerview.adapter = adapter
                        }
                    }
                }

                else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
        }
    }
}
