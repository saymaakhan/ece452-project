package com.example.ace.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.calendar.extensions.getDot
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.example.ace.MainActivity
import com.example.ace.R
import com.example.ace.databinding.ActivityCalendarBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import java.io.File
import androidx.core.content.FileProvider
import biweekly.property.DateStart
import biweekly.property.Summary

class CalendarActivity : AppCompatActivity(), OnDayClickListener, OnSelectDateListener {

    private lateinit var binding: ActivityCalendarBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient

    // A mutable map to store notes for specific EventDays
    private val notes = mutableMapOf<EventDay, String>()

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the FAB button to open the date picker
        binding.fabButton.setOnClickListener { openDatePicker() }
        binding.calendarView.setOnDayClickListener(this)

        // Set the current activity as the listener for day clicks on the calendar
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val logout = binding.logout

        logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }

        // Button listening to evoke function
        binding.icsButton.setOnClickListener { createICSFile() }
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

    // Create the ICS file and make it a clickable link
    private fun createICSFile() {
        val iCalendar = ICalendar()

        // Iterate EventDay map and create exportable file
        for (entry in notes.entries) {
            val eventDay = entry.key
            val note = entry.value

            val vEvent = VEvent()
            val startDate: Date = eventDay.calendar.time
            val dateStart = DateStart(startDate)
            vEvent.dateStart = dateStart

            // Set the event summary as the note content
            vEvent.summary = Summary(note)

            iCalendar.addEvent(vEvent)
        }

        // iCalendar object
        val fileName = "events.ics"
        val icsFile = File(filesDir, fileName)

        icsFile.bufferedWriter().use { writer ->
            Biweekly.write(iCalendar).go(writer)
        }

        // Create URI for the ICS
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", icsFile)

        // Usable apps and permissions
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/calendar"
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Export file
        startActivity(Intent.createChooser(intent, "Export ICS"))
    }
}