package com.example.ace.ui.grades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ace.R
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class CreateClassActivity : AppCompatActivity() {

    private var courseItemCount = 0
    private lateinit var containerCourseItems: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_class)

        containerCourseItems = findViewById(R.id.containerCourseItems)

        val btnAddItem: Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            addCourseItemView()
        }

    }

    private fun addCourseItemView() {
        // Inflate the layout for the course syllabus item
        val itemView: View = layoutInflater.inflate(R.layout.course_item_layout, null, false)

        // Get references to the views within the course syllabus item layout
        val etCourseName: EditText = itemView.findViewById(R.id.etCourseName)
        val etCourseWeight: EditText = itemView.findViewById(R.id.etCourseWeight)

        // Set unique IDs for the views to differentiate each item
        val uniqueId = View.generateViewId()
        etCourseName.id = uniqueId
        etCourseWeight.id = uniqueId + 1000

        // Add the course syllabus item view to the container
        containerCourseItems.addView(itemView)

        // Increment the count to track the number of course syllabus items
        courseItemCount++
    }

    private fun saveClassToFirestore(className: String) {
        // ...

        // Iterate through the dynamically added views and save the data to Firestore
        for (i in 0 until courseItemCount) {
            val courseId = containerCourseItems.getChildAt(i).id
            val etCourseName: EditText = findViewById(courseId)
            val etCourseWeight: EditText = findViewById(courseId + 1000)

            val courseName = etCourseName.text.toString()
            val courseWeight = etCourseWeight.text.toString().toDouble()

            // Save the courseName and courseWeight to Firestore for each item
            // You can customize this based on your Firestore structure
        }
    }

    // Rest of your code...
}
