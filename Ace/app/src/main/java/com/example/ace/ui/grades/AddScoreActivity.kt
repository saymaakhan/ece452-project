package com.example.ace.ui.grades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.ace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal
import java.math.RoundingMode

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
            val scoreDocumentRef = syllabusDocumentRef?.collection("score_items")?.document(itemName)

            var grade = if (percentage.isNotEmpty()) percentage.toDouble() else ((gradeNumerator.toDouble() / gradeDenominator.toDouble()) * 100)

            grade = BigDecimal(grade.toString()).setScale(2, RoundingMode.HALF_EVEN).toDouble()

            val scoreItem = ScoreItem(itemName, grade)

            scoreDocumentRef?.set(scoreItem)?.addOnSuccessListener {
                val scoreEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerScore, false)

                scoreEntryView.findViewById<TextView>(R.id.tvClassName).text = itemName
                scoreEntryView.findViewById<TextView>(R.id.tvWeight).text = "Weight: $grade"
                containerScore.addView(scoreEntryView)

                updateGradesOnFireStore()

            }?.addOnFailureListener {
                Toast.makeText(this, "Error retrieving syllabus items: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadClassesFromFirestore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val classDocumentRef = className?.let {
                firestore.collection("classes").document(userId).collection("user_classes")
                    .document(it)
            }
            val syllabusDocumentRef = syllabusItemName?.let {
                classDocumentRef?.collection("syllabus_items")?.document(it)
            }
            val scoreDocumentRef = syllabusDocumentRef?.collection("score_items")

            scoreDocumentRef?.get()?.addOnSuccessListener {
                containerScore.removeAllViews()

                for (documentSnapshot in it) {
                    if (documentSnapshot.exists()) {
                        val scoreItemName = documentSnapshot.data["scoreItemName"]
                        val grade = documentSnapshot.data["grade"]

                        val scoreEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerScore, false)

                        scoreEntryView.findViewById<TextView>(R.id.tvClassName).text = scoreItemName as CharSequence?
                        scoreEntryView.findViewById<TextView>(R.id.tvWeight).text = "Grade: $grade"
                        containerScore.addView(scoreEntryView)
                    }
                }
            }
        }
    }

    private fun updateGradesOnFireStore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        if (userId != null) {
            className?.let {className ->
                syllabusItemName?.let { syllabusItemName ->
                    val syllabusItemRef = firestore.collection("classes")
                        .document(userId)
                        .collection("user_classes")
                        .document(className)
                        .collection("syllabus_items")
                        .document(syllabusItemName)

                    firestore.collection("classes")
                        .document(userId)
                        .collection("user_classes")
                        .document(className)
                        .collection("syllabus_items")
                        .document(syllabusItemName)
                        .collection("score_items")
                        .get()
                        .addOnSuccessListener {
                            var scoresCounter = 0.0
                            var scoresSum = 0.0
                            for (scores in it) {
                                if (scores.exists()) {
                                    scoresCounter += 1
                                    scoresSum += scores["grade"] as Double
                                }
                            }
                            val syllabusGrade = scoresSum / scoresCounter

                            syllabusItemRef.update("syllabusGrade", syllabusGrade)
                        }
                }
            }
        }
    }
}