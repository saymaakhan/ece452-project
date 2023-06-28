package com.example.ace.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.calendar.extensions.getDot
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.example.ace.R
import com.example.ace.databinding.ActivityCalendarBinding
import java.util.*

class CalendarActivity : AppCompatActivity(), OnDayClickListener, OnSelectDateListener {

    private lateinit var binding: ActivityCalendarBinding

    // A mutable map to store notes for specific EventDays
    private val notes = mutableMapOf<EventDay, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the FAB button to open the date picker
        binding.fabButton.setOnClickListener { openDatePicker() }

        // Set the current activity as the listener for day clicks on the calendar
        binding.calendarView.setOnDayClickListener(this)
    }

    // Open the date picker dialog when the FAB button is clicked
    private fun openDatePicker() {
        DatePickerBuilder(this, this)
            .pickerType(CalendarView.ONE_DAY_PICKER)
            .headerColor(R.color.primary)
            .todayLabelColor(R.color.secondary)
            .selectionColor(R.color.secondary_light)
            .dialogButtonsColor(R.color.secondary)
            .build()
            .show()
    }

    // Handle the click event for a specific day on the calendar
    override fun onDayClick(eventDay: EventDay) {
        // Start the NotePreviewActivity and pass the selected calendar date and associated note
        val intent = Intent(this, NotePreviewActivity::class.java)
        intent.putExtra(CALENDAR_EXTRA, eventDay.calendar)
        intent.putExtra(NOTE_EXTRA, notes[eventDay])
        startActivity(intent)
    }

    // Handle the selection of one or more dates in the date picker
    override fun onSelect(calendar: List<Calendar>) {
        // Start the AddNoteActivity and pass the selected calendar date as an extra
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra(CALENDAR_EXTRA, calendar.first())
        startActivityForResult(intent, RESULT_CODE)
    }

    // Handle the result returned from the AddNoteActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val note = data?.getStringExtra(NOTE_EXTRA) ?: return
            val calendar = data.getSerializableExtra(CALENDAR_EXTRA) as Calendar

            // Create an EventDay with the selected calendar date and a dot indicating a note is available
            val eventDay = EventDay(calendar, applicationContext.getDot())

            // Store the note in the map with the associated EventDay
            notes[eventDay] = note

            // Update the events on the calendar view with the new list of EventDays
            binding.calendarView.setEvents(notes.keys.toList())
        }
    }

    companion object {
        const val CALENDAR_EXTRA = "calendar"
        const val NOTE_EXTRA = "note"
        const val RESULT_CODE = 8
    }
}
