package com.example.ace.ui.grades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.math.BigDecimal
import java.math.RoundingMode

data class ClassInfo(
    var className: String? = null,
    var grade: Double? = 420.0
)

data class UserInfo(
    var uid: String? =null
)

class GradesActivity : AppCompatActivity(), AddClassDialogFragment.OnSaveClickListener {

    private lateinit var containerClasses: LinearLayout
    private lateinit var tvCumulativeAverage: TextView
    private lateinit var tvNoClassesMessage: TextView

    override fun onResume() {
        super.onResume()
        loadClassesFromFirestore()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)

        containerClasses = findViewById(R.id.containerClasses)
        tvCumulativeAverage = findViewById(R.id.tvCumulativeAverage)
        tvNoClassesMessage = findViewById(R.id.tvNoClassesMessage)

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
                    classEntryView.findViewById<TextView>(R.id.tvWeight).text = "Grade: ??.??%"
                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_unknown)

                    containerClasses.addView(classEntryView)

                    tvNoClassesMessage.visibility = View.GONE
                    containerClasses.visibility = View.VISIBLE

                    val userObject = UserInfo(userId)
                    firestore.collection("user_enrolled_classes").document(className).collection("users").document(userId).set(userObject)

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

                    var gradesSum = 0.0
                    var classCounter = 0.0

                    // Iterate through the class documents and display them
                    for (documentSnapshot in querySnapshot) {
                        if (documentSnapshot.exists()) {
                            tvNoClassesMessage.visibility = View.GONE
                            containerClasses.visibility = View.VISIBLE

                            documentSnapshot.data["className"]?.let { it ->
                                // Create a new TextView to represent the class name
                                val className = it as CharSequence?
                                val classEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerClasses, false)
                                classEntryView.findViewById<TextView>(R.id.tvClassName).text = className
                                val grade = BigDecimal(documentSnapshot.data["grade"].toString()).setScale(2, RoundingMode.HALF_EVEN)
                                classEntryView.findViewById<TextView>(R.id.tvWeight).text = if (documentSnapshot.data["grade"] == 420.0) "Grade: ??.?? %" else "Grade: $grade%"

                                if (grade.toDouble() == 420.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_unknown)
                                } else if (grade.toDouble() > 95.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_a_plus)
                                } else if (grade.toDouble() > 87.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_a)
                                } else if (grade.toDouble() > 80.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_a_minus)
                                } else if (grade.toDouble() > 77.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_b_plus)
                                } else if (grade.toDouble() > 73.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_b)
                                } else if (grade.toDouble() > 70.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_b_minus)
                                } else if (grade.toDouble() > 67.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_c_plus)
                                } else if (grade.toDouble() > 63.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_c)
                                } else if (grade.toDouble() > 60.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_c_minus)
                                } else if (grade.toDouble() > 57.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_d_plus)
                                } else if (grade.toDouble() > 53.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_d)
                                } else if (grade.toDouble() > 50.0) {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_d_minus)
                                } else {
                                    classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_f)
                                }

                                containerClasses.addView(classEntryView)

                                // Set an onClickListener for the class entry to open the GradesActivity
                                classEntryView.setOnClickListener {
                                    // Launch the activity to add grades for the selected class
                                    val intent = Intent(this, AddGradesActivity::class.java)
                                    intent.putExtra("class_name", className)
                                    startActivity(intent)
                                }
                            }

                            if (documentSnapshot.data["grade"] != 420.0) {
                                classCounter += 1.0
                                gradesSum += BigDecimal(documentSnapshot.data["grade"].toString()).toDouble()
                            }
                        }
                    }

                    if (classCounter > 0) {
                        val cumulativeGrade = gradesSum / classCounter
                        val roundCumulativeGrade = BigDecimal(cumulativeGrade.toString()).setScale(2, RoundingMode.HALF_EVEN)
                        tvCumulativeAverage.text = "Cumulative Average: $roundCumulativeGrade%"
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