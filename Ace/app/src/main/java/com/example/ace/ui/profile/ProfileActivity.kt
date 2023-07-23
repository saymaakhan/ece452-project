package com.example.ace.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show()
    }
}