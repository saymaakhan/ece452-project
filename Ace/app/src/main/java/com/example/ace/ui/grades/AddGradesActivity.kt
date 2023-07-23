package com.example.ace.ui.grades

import android.annotation.SuppressLint
import android.content.Intent
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
import java.math.BigDecimal
import java.math.RoundingMode

data class SyllabusItem(
    var syllabusItemName: String? = null,
    var weight: Double? = 0.0,
    var syllabusGrade: Double? = 420.0
)

class AddGradesActivity : AppCompatActivity(), AddSyllabusItemDialogFragment.OnSaveClickListener {

    private var className: String? = null
    private lateinit var containerSyllabus: LinearLayout

    override fun onResume() {
        super.onResume()
        loadClassesFromFirestore()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_grades)

        containerSyllabus = findViewById(R.id.containerSyllabus)

        className = intent.getStringExtra("class_name")

        // Set the class name as the title in the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = className

        loadClassesFromFirestore()

        // Set click listener for "Add Syllabus Item" button
        findViewById<Button>(R.id.btnAddSyllabusItem).setOnClickListener {
            val addSyllabusItemDialog = AddSyllabusItemDialogFragment()
            addSyllabusItemDialog.setOnSaveClickListener(this)
            addSyllabusItemDialog.show(supportFragmentManager, "AddSyllabusItemDialogFragment")
        }
    }

    override fun onSaveClicked(itemName: String, itemWeight: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val classDocumentRef = className?.let {
                firestore.collection("classes").document(userId).collection("user_classes").document(it)
            }

            val syllabusDocumentRef = classDocumentRef?.collection("syllabus_items")?.document(itemName)

            val syllabusItem = SyllabusItem(itemName, itemWeight.toDouble())

            syllabusDocumentRef?.set(syllabusItem)
                ?.addOnSuccessListener {
                    val syllabusEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerSyllabus, false)

                    syllabusEntryView.findViewById<TextView>(R.id.tvClassName).text = itemName
                    syllabusEntryView.findViewById<TextView>(R.id.tvWeight).text = "-- / $itemWeight: ??%"

                    containerSyllabus.addView(syllabusEntryView)

                    // Set an onClickListener for the class entry to open the GradesActivity
                    syllabusEntryView.setOnClickListener {
                        // Launch the activity to add grades for the selected class
                        val intent = Intent(this, AddScoreActivity::class.java)
                        intent.putExtra("syllabus_name", itemName)
                        intent.putExtra("class_name", className)
                        startActivity(intent)
                    }

                    Toast.makeText(this, "Syllabus item saved successfully", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Error retrieving syllabus items: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadClassesFromFirestore() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user is logged in and the UID is not null
        if (userId != null) {
            // Get a reference to the user's document in the "classes" collection
            val firestore = FirebaseFirestore.getInstance()
            val classDocumentRef = className?.let { firestore.collection("classes").document(userId).collection("user_classes").document(it) }
            val syllabusDocumentRef = classDocumentRef?.collection("syllabus_items")

            syllabusDocumentRef?.get()?.addOnSuccessListener {
                containerSyllabus.removeAllViews()

                for (documentSnapshot in it) {
                    if (documentSnapshot.exists()) {
                        val syllabusItemName = documentSnapshot.data["syllabusItemName"]
                        val weight = documentSnapshot.data["weight"]
                        val syllabusGrade = documentSnapshot.data["syllabusGrade"]

                        val syllabusEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerSyllabus, false)

                        syllabusEntryView.findViewById<TextView>(R.id.tvClassName).text = syllabusItemName as CharSequence?

                        if (syllabusGrade == 420.0) {
                            syllabusEntryView.findViewById<TextView>(R.id.tvWeight).text = "-- / $weight: ??%"
                        } else {
                            val weightPercentage = BigDecimal(weight.toString()) // Convert weight to BigDecimal
                            val weightFraction = weightPercentage.divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN) // Divide by 100
                            val weightGrade = BigDecimal(syllabusGrade.toString()).multiply(weightFraction)
                            val roundedWeightGrade = weightGrade.setScale(2, RoundingMode.HALF_EVEN)

                            syllabusEntryView.findViewById<TextView>(R.id.tvWeight).text = "$roundedWeightGrade / $weight: $syllabusGrade%"
                        }
                        containerSyllabus.addView(syllabusEntryView)

                        // Set an onClickListener for the class entry to open the GradesActivity
                        syllabusEntryView.setOnClickListener {
                            // Launch the activity to add grades for the selected class
                            val intent = Intent(this, AddScoreActivity::class.java)
                            intent.putExtra("syllabus_name", syllabusItemName)
                            intent.putExtra("class_name", className)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}