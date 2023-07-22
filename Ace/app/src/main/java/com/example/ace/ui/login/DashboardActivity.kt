package com.example.ace.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.MainActivity
import com.example.ace.R
import com.example.ace.calendar.CalendarActivity
import com.example.ace.databinding.ActivityDashboardBinding
import com.example.ace.ui.camera.CameraActivity
import com.example.ace.ui.chat.ChatContacts
import com.example.ace.ui.chat.DiscussionForumTopics
import com.example.ace.ui.grades.GradesActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityDashboardBinding

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // call requestIdToken as follows
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val logout = binding.logout
        val calendar = binding.calendar
        val camera = binding.camera
        val grades = binding.grades
        val profile = binding.profile
        val dm = binding.dm
        val forum = binding.forum

        logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }

        calendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        grades.setOnClickListener {
            val intent = Intent(this, GradesActivity::class.java)
            startActivity(intent)
        }

        dm.setOnClickListener {
            val intent = Intent(this, ChatContacts::class.java)
            startActivity(intent)
        }

        forum.setOnClickListener {
            val intent = Intent(this, DiscussionForumTopics::class.java)
            startActivity(intent)
        }
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else "anonymous"
    }
}
