package com.example.ace.ui.grades

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

data class SyllabusItem(
    var syllabusItemName: String? = null,
    var weight: Int? = 0,
    var syllabusGrade: Int? = 100
)

class AddGradesActivity : AppCompatActivity(), AddSyllabusItemDialogFragment.OnSaveClickListener {

    private var className: String? = null
    private lateinit var containerSyllabus: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_grades)

        containerSyllabus = findViewById(R.id.containerSyllabus)

        className = intent.getStringExtra("class_name")

        // Set the class name as the title in the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = className

        // Set click listener for "Add Syllabus Item" button
        findViewById<Button>(R.id.btnAddSyllabusItem).setOnClickListener {
            val addSyllabusItemDialog = AddSyllabusItemDialogFragment()
            addSyllabusItemDialog.setOnSaveClickListener(this)
            addSyllabusItemDialog.show(supportFragmentManager, "AddSyllabusItemDialogFragment")
        }
    }

    override fun onSaveClicked(itemName: String, itemWeight: String) {
        println("Syllabus Item Name: $itemName, Weight: $itemWeight")

        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val classDocumentRef = className?.let {
                firestore.collection("classes").document(userId).collection("user_classes").document(it)
            }

            val syllabusDocumentRef = classDocumentRef?.collection("syllabus_item")?.document(itemName)

            val syllabusItem = SyllabusItem(itemName, itemWeight.toInt())

            syllabusDocumentRef?.set(syllabusItem)
                ?.addOnSuccessListener {
                    val syllabusEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerSyllabus, false)
                    syllabusEntryView.findViewById<TextView>(R.id.tvClassName).text = itemName
                    containerSyllabus.addView(syllabusEntryView)

                    // Set an onClickListener for the class entry to open the GradesActivity
                    syllabusEntryView.setOnClickListener {
                        // Launch the activity to add grades for the selected class
                        val intent = Intent(this, AddGradesActivity::class.java)
                        intent.putExtra("syllabus_name", itemName)
                        startActivity(intent)
                    }

                    Toast.makeText(this, "Syllabus item saved successfully", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Error retrieving syllabus items: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}