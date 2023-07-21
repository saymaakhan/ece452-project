package com.example.ace.ui.grades

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GradesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)

        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val userDocumentRef = userId?.let { firestore.collection("users").document(it) }

        userDocumentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Document exists, get the email from the document
                val userEmail = documentSnapshot.getString("email")
                Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show()
            }
        }?.addOnFailureListener { exception ->
            Log.e(TAG, "Error retrieving user document: $exception")
        }

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        fabAdd.setOnClickListener {
            // Launch the CreateClassActivity
            val intent = Intent(this, CreateClassActivity::class.java)
            startActivity(intent)
        }

    }
}