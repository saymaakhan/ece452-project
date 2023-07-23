package com.example.ace.ui.grades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.ace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class ScoreItem(
    val scoreItemName: String? = null,
    val grade: Double = 0.0
)
class AddScoreActivity : AppCompatActivity(), AddItemGradeDialogFragment.OnSaveClickListener {

    private var className: String? = null
    private var syllabusItemName: String? = null
    private lateinit var containerScore: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_score)

        className = intent.getStringExtra("class_name")
        syllabusItemName = intent.getStringExtra("syllabus_name")
        containerScore = findViewById(R.id.containerScore)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = syllabusItemName

        loadClassesFromFirestore()

        findViewById<Button>(R.id.btnAddScoreItem).setOnClickListener {
            val addItemGradeDialog = AddItemGradeDialogFragment()
            addItemGradeDialog.setOnSaveClickListener(this)
            addItemGradeDialog.show(supportFragmentManager, "AddItemGradeDialogFragment")
        }
    }

    override fun onSaveClicked(itemName: String, gradeNumerator: String, gradeDenominator: String, percentage: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val classDocumentRef = className?.let {
                firestore.collection("classes").document(userId).collection("user_classes").document(it)
            }
            val syllabusDocumentRef = syllabusItemName?.let {
                classDocumentRef?.collection("syllabus_items")?.document(it)
            }
            val scoreDocumentRef = syllabusDocumentRef?.collection("score_items").document(itemName)

            val grade = if (percentage.toDouble() > 0) percentage.toDouble() else (gradeNumerator.toDouble() / gradeDenominator.toDouble())

            val scoreItem = ScoreItem(itemName, grade)

            scoreDocumentRef.set(scoreItem).addOnSuccessListener {
                val scoreEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerScore, false)

                scoreEntryView.findViewById<TextView>(R.id.tvClassName).text = itemName
                scoreEntryView.findViewById<TextView>(R.id.tvWeight).text = "Weight: $grade"
                containerScore.addView(scoreEntryView)

                Toast.makeText(this, "Score item saved successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error retrieving syllabus items: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadClassesFromFirestore() {

    }
}