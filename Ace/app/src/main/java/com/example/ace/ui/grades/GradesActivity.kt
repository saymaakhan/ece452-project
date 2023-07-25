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
import android.app.Dialog
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.ace.ui.profile.ProfileActivity

private lateinit var btnInsights: Button

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

    private var lowestGrade: Double = Double.MAX_VALUE
    private var highestGrade: Double = Double.MIN_VALUE
    private var lowestCourse:String = ""
    private var highestCourse:String = ""

    private val openAIAPIKey: String = "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"

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

        val spannableString = SpannableString("You have no classes at the moment.\nGo to My Profile to add courses.")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@GradesActivity, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true // Underline the text to make it look like a link
                ds.color = ContextCompat.getColor(this@GradesActivity, R.color.teal_200)
            }
        }

        spannableString.setSpan(clickableSpan, 41, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvNoClassesMessage.text = spannableString
        tvNoClassesMessage.movementMethod = LinkMovementMethod.getInstance()

        loadClassesFromFirestore()

        // Find Insights button and listen
        btnInsights = findViewById(R.id.btnInsights)
        btnInsights.setOnClickListener {
            showInsightsModal()
        }

    }

    private fun showInsightsModal() {
        // Create
        val dialog = Dialog(this)

        // Set the content view to the custom dialog layout
        dialog.setContentView(R.layout.dialog_insights)

        val text_dialog = dialog.findViewById<TextView>(R.id.text_dialog)
        if (highestGrade == Double.MIN_VALUE) {
            text_dialog.text = "At this moment,\n there isn't sufficient \ninformation available to provide \ninsights on your grades"
        } else {
            text_dialog.text = "Great job in \n" + highestCourse + "!\n You are " +
                    "excelling. \n\n Now, it is recommended you focus on studying " +
                    "for \n" +lowestCourse + ". \n\n Good luck!"
        }

        dialog.show()
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
                    firestore.collection("user_enrolled_classes").document(className).set(hashMapOf("test" to 1))

                    // Set an onClickListener for the class entry to open the GradesActivity
                    classEntryView.setOnClickListener {
                        // Launch the activity to add grades for the selected class
                        val intent = Intent(this, AddGradesActivity::class.java)
                        intent.putExtra("class_name", className)
                        startActivity(intent)
                    }
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

                                // Calculate the lowest grade
                                if (grade.toDouble() < lowestGrade) {
                                    lowestGrade = grade.toDouble()
                                    lowestCourse = className.toString()
                                }

                                // Calculate the highest grade
                                if (grade.toDouble() > highestGrade && grade.toDouble() != 420.0) {
                                    highestGrade = grade.toDouble()
                                    highestCourse = className.toString()
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