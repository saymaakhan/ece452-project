package com.example.ace.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.ace.calendar.CalendarActivity.Companion.CALENDAR_EXTRA
import com.example.ace.calendar.CalendarActivity.Companion.NOTE_EXTRA
//import com.applandeo.calendarsampleapp.databinding.ActivityNotePreviewBinding
import com.example.ace.calendar.extensions.toSimpleDate
import com.example.ace.databinding.ActivityNotePreviewBinding
import java.util.*

class NotePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = intent.getSerializableExtra(CALENDAR_EXTRA) as Calendar
        binding.toolbar.subtitle = calendar.time.toSimpleDate()

        val eventTitle = intent.getStringExtra(NOTE_EXTRA)

        if (eventTitle != null) {
            binding.noteTextView.text = eventTitle
            binding.emptyStateTextView.isVisible = false
        }
    }
}