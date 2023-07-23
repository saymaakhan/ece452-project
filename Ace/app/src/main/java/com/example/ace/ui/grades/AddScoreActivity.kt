package com.example.ace.ui.grades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.ace.R

class AddScoreActivity : AppCompatActivity() {

    private var syllabusItemName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_score)

        syllabusItemName = intent.getStringExtra("syllabus_name")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = syllabusItemName
    }
}