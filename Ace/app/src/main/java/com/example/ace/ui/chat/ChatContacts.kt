package com.example.ace.ui.chat

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R
import com.example.ace.databinding.ActivityChatContactsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

class ChatContacts : AppCompatActivity() {
    private lateinit var binding: ActivityChatContactsBinding
    private lateinit var auth: FirebaseAuth

    var users : MutableList<String> = mutableListOf()
    private var job: Job = Job()

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoPictureView : ImageView =  findViewById(R.id.logo_icon)
        logoPictureView.setImageResource(R.drawable.loginlogo)
        val iconPictureView : ImageView =  findViewById(R.id.icon_account_picture)
        iconPictureView.setImageResource(R.drawable.account_circle_48px__1_)

        auth = Firebase.auth
        loadUsersFromFirestore()

    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else SingleChat.ANONYMOUS
    }

    private fun loadUsersFromFirestore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_chat_contacts)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val currUser = getUserName()
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val userName = document.data["displayName"].toString()
                        if (userName != currUser) {
                            users.add(document.data["displayName"].toString())
                        }
                    }
                    val adapter = ChatContactAdapter(users) { user: String, position: Int ->
                        val name = user
                        val intent = Intent(this, SingleChat::class.java)
                        intent.putExtra("ChatUser", name)
                        startActivity(intent)
                        finish()
                    }
                        recyclerview.adapter = adapter
                    }

                 else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
        }
    }
}