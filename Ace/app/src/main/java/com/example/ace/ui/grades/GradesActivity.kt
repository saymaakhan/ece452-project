package com.example.ace.ui.grades

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class ClassInfo(
    var className: String? = null,
    var grade: Int? = 100
)

class GradesActivity : AppCompatActivity(), AddClassDialogFragment.OnSaveClickListener {

    private lateinit var containerClasses: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)

        containerClasses = findViewById(R.id.containerClasses)

        loadClassesFromFirestore()

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        fabAdd.setOnClickListener {
            // Show the AddClassDialogFragment
            val addClassDialog = AddClassDialogFragment()
            addClassDialog.setOnSaveClickListener(this)
            addClassDialog.show(supportFragmentManager, "AddClassDialogFragment")
        }

    }

    override fun onSaveClicked(className: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user is logged in and the UID is not null
        if (userId != null) {
            // Get a reference to the user's document in the "classes" collection
            val firestore = FirebaseFirestore.getInstance()
            val userDocumentRef = firestore.collection("classes").document(userId)

            // Create a new class document with a unique identifier (class name in this case)
            val classDocumentRef = userDocumentRef.collection("user_classes").document(className)

            // Create a ClassInfo object to store the class data
            val classInfo = ClassInfo(className)

            // Set the data of the new class document
            classDocumentRef.set(classInfo)
                .addOnSuccessListener {
                    val classEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerClasses, false)
                    classEntryView.findViewById<TextView>(R.id.tvClassName).text = className
                    classEntryView.findViewById<TextView>(R.id.tvWeight).text = "Grade: ${classInfo.grade}"

                    containerClasses.addView(classEntryView)

                    // Set an onClickListener for the class entry to open the GradesActivity
                    classEntryView.setOnClickListener {
                        // Launch the activity to add grades for the selected class
                        val intent = Intent(this, AddGradesActivity::class.java)
                        intent.putExtra("class_name", className)
                        startActivity(intent)
                    }

                    Toast.makeText(this, "Class saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Error saving class data
                    Toast.makeText(this, "Error saving class: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

        } else {
            // User is not logged in, handle this case if needed
        }
    }

    private fun loadClassesFromFirestore() {
        // Get the user's UID
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user is logged in and the UID is not null
        if (userId != null) {
            // Get a reference to the user's document in the "classes" collection
            val firestore = FirebaseFirestore.getInstance()
            val userDocumentRef = firestore.collection("classes").document(userId)

            // Get all class documents from the subcollection
            userDocumentRef.collection("user_classes").get()
                .addOnSuccessListener { querySnapshot ->
                    // Clear existing views before displaying new classes
                    containerClasses.removeAllViews()

                    // Iterate through the class documents and display them
                    for (documentSnapshot in querySnapshot) {
                        if (documentSnapshot.exists()) {
                            documentSnapshot.data["className"]?.let { it ->
                                // Create a new TextView to represent the class name
                                val className = it as CharSequence?
                                val classEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerClasses, false)
                                classEntryView.findViewById<TextView>(R.id.tvClassName).text = className
                                classEntryView.findViewById<TextView>(R.id.tvWeight).text = "Grade: ${documentSnapshot.data["grade"]}"
                                containerClasses.addView(classEntryView)

                                // Set an onClickListener for the class entry to open the GradesActivity
                                classEntryView.setOnClickListener {
                                    // Launch the activity to add grades for the selected class
                                    val intent = Intent(this, AddGradesActivity::class.java)
                                    intent.putExtra("class_name", className)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Error retrieving classes
                    Toast.makeText(this, "Error retrieving classes: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // User is not logged in, handle this case if needed
        }
    }
}