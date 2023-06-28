package com.example.ace.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.calendar.CalendarActivity.Companion.CALENDAR_EXTRA
import com.example.ace.calendar.CalendarActivity.Companion.NOTE_EXTRA
//import com.applandeo.calendarsampleapp.databinding.ActivityAddNoteBinding
import com.applandeo.calendarsampleapp.extensions.toSimpleDate
import com.example.ace.databinding.ActivityAddNoteBinding
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the selected calendar date from the intent
        val calendar = intent.getSerializableExtra(CALENDAR_EXTRA) as Calendar

        // Set the calendar date as a subtitle in the toolbar
        binding.toolbar.subtitle = calendar.time.toSimpleDate()

        // Handle the save button click event
        binding.saveButton.setOnClickListener {
            // Get the note text from the EditText field
            val note = binding.noteEditText.text.toString()

            // Check if the note is not empty
            if (note.isNotEmpty()) {
                // Create a return intent with the calendar date and note as extras
                val returnIntent = Intent()
                returnIntent.putExtra(CALENDAR_EXTRA, calendar)
                returnIntent.putExtra(NOTE_EXTRA, note)

                // Set the result as RESULT_OK and pass the return intent
                setResult(RESULT_OK, returnIntent)
            }

            // Finish the activity and return to the calling activity
            finish()
        }
    }
}
