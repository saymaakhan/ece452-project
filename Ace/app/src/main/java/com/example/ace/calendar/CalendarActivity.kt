package com.example.ace.calendar

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import com.example.ace.ui.profile.ProfileActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.ace.calendar.extensions.getDot
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.applandeo.materialcalendarview.utils.calendar
import com.example.ace.MainActivity
import com.example.ace.R
import com.example.ace.databinding.ActivityCalendarBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.app.Dialog
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import com.example.ace.calendar.extensions.getDot
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

        // Get Firebase Info & Render Events
        val userId = auth.currentUser?.uid
        
        // Set the current activity as the listener for day clicks on the calendar
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //fetchUserEvents()
        fetchEventsFromFirestore(userId)
        val profile = binding.profile

        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchEventsFromFirestore(userId: String?){
        // Check if the user is logged in
        if (userId != null) {
            // Get a reference to the Firestore collection "calendar_events" for the current user
            val firestore = FirebaseFirestore.getInstance()
            val userDocumentRef = firestore.collection("users").document(userId)
            val calendarEventsCollectionRef = userDocumentRef.collection("calendar_events")

            // Fetch events for the current user from Firestore
            calendarEventsCollectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val events = mutableListOf<EventDay>()

                    for (document in querySnapshot.documents) {
                        val eventTitle = document.getString("eventTitle")
                        val eventDate = document.getString("eventDate")
                        if (eventTitle != null && eventDate != null) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val eventDate = dateFormat.parse(eventDate) ?: Date()

                            val eventDay = EventDay(Calendar.getInstance().apply { time = eventDate }, applicationContext.getDot())
                            events.add(eventDay)

                            notes[eventDay] = "$eventTitle at $eventDate"

                        }
                    }

                    // Recreate the events on the calendar view with the new list of EventDays
                    binding.calendarView.setEvents(events)
                }
                .addOnFailureListener { exception ->
                    // Error fetching events
                    // ... (handle error)
                }
        }
    }





    // Open the date picker dialog when the FAB button is clicked
    private fun openDatePicker() {
        DatePickerBuilder(this, this)
            .pickerType(CalendarView.ONE_DAY_PICKER)
            .headerColor(R.color.gray)
            .pagesColor(R.color.gray)
            .daysLabelsColor(R.color.white)
            .abbreviationsBarColor(R.color.gray)
            .abbreviationsLabelsColor(R.color.white)
            .todayLabelColor(R.color.secondary)
            .selectionColor(R.color.secondary_light)
            .dialogButtonsColor(R.color.secondary)
            .build()
            .show()
    }

    // Handle the click event for a specific day on the calendar
    override fun onDayClick(eventDay: EventDay) {
        // Start the NotePreviewActivity and pass the selected calendar date and associated eventTitle
        val intent = Intent(this, NotePreviewActivity::class.java)
        intent.putExtra(CALENDAR_EXTRA, eventDay.calendar)

        // Get the associated eventTitle from the notes map and pass it to the NotePreviewActivity
        val eventTitle = notes[eventDay]
        intent.putExtra(NOTE_EXTRA, eventTitle)
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
            val eventTitle = data?.getStringExtra(NOTE_EXTRA) ?: return
            val calendar = data.getSerializableExtra(CALENDAR_EXTRA) as Calendar

            // Convert the calendar date to a string representation (you can adjust the format as needed)
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            // Get a reference to the Firestore collection "calendar_events" for the current user
            val firestore = FirebaseFirestore.getInstance()
            val userId = auth.currentUser?.uid ?: return
            val eventDocumentRef = firestore.collection("users").document(userId)
                .collection("calendar_events").document(dateString)

            // Create a map to represent the event data
            val eventMap = hashMapOf(
                "eventTitle" to eventTitle,
                "eventDate" to dateString// You can also save the timestamp when the event was added
            )

            // Set the event data in the document
            eventDocumentRef.set(eventMap)
                .addOnSuccessListener {
                    // Event data saved successfully
                    val eventDay = EventDay(calendar, applicationContext.getDot())
                    notes[eventDay] = eventTitle
                    binding.calendarView.setEvents(notes.keys.toList())
                }
                .addOnFailureListener { exception ->
                    // Error saving event data
                    Toast.makeText(this, "Error saving event: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /*
    private fun fetchUserEvents() {
        // Get a reference to the Firestore collection "calendar_events" for the current user
        val firestore = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid ?: return

        val eventsCollectionRef = firestore.collection("users").document(userId)
            .collection("calendar_events")

        // Clear existing events before fetching new ones
//        notes.clear()

        // Fetch all events for the user
        eventsCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {
                    // Get the event data from the document
                    val eventTitle = documentSnapshot.getString("eventTitle")
                    val eventDateTimestamp = documentSnapshot.getTimestamp("eventDate")

                    // Convert the eventDateTimestamp to a Calendar object
                    val eventDateCalendar = Calendar.getInstance()
                    eventDateCalendar.timeInMillis = eventDateTimestamp?.toDate()?.time ?: continue

                    // Create an EventDay with the event date and a dot indicating an event is available
                    val eventDay = EventDay(eventDateCalendar, applicationContext.getDot())

                    // Store the event in the map with the associated EventDay
                    eventTitle?.let { title -> notes[eventDay] = title }
                }

                // Update the events on the calendar view with the new list of EventDays
                binding.calendarView.setEvents(notes.keys.toList())
            }
            .addOnFailureListener { exception ->
                // Error fetching events
                Toast.makeText(this, "Error fetching events: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
*/

    companion object {
        const val CALENDAR_EXTRA = "calendar"
        const val NOTE_EXTRA = "note"
        const val RESULT_CODE = 8
    }

    private fun generateICSContent(): String {
        val icsBuilder = StringBuilder()
        icsBuilder.appendLine("BEGIN:VCALENDAR")
        icsBuilder.appendLine("VERSION:2.0")
        icsBuilder.appendLine("PRODID:-//Your Company//Your App//EN")

        for (eventDay in notes.keys) {
            val eventTitle = notes[eventDay] ?: continue
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val dateString = dateFormat.format(eventDay.calendar.time)
            icsBuilder.appendLine("BEGIN:VEVENT")
            icsBuilder.appendLine("DTSTART:$dateString")
            icsBuilder.appendLine("SUMMARY:$eventTitle")
            icsBuilder.appendLine("END:VEVENT")
        }

        icsBuilder.appendLine("END:VCALENDAR")
        return icsBuilder.toString()
    }

    private fun saveICSFile(icsContent: String): File? {
        val externalFilesDir = getExternalFilesDir(null)
        val icsFile = File(externalFilesDir, "calendar_events.ics")
        return try {
            icsFile.writeText(icsContent)
            icsFile
        } catch (e: IOException) {
            null
        }
    }

    fun createICSFile(view: View) {
        val icsContent = generateICSContent()
        val icsFile = saveICSFile(icsContent)
        if (icsFile != null) {
            // Show the custom dialog with the download link
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_download_ics)
            val downloadLink = dialog.findViewById<TextView>(R.id.downloadLink)
            val icsFileUri = FileProvider.getUriForFile(
                this,
                "com.example.ace.fileprovider",
                icsFile
            )
            downloadLink.text = "Export to Google Calendar"
            downloadLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(icsFileUri, "text/calendar")
                startActivity(intent)
            }
            dialog.show()
        } else {
            Toast.makeText(this, "Error generating ICS file", Toast.LENGTH_SHORT).show()
        }
    }


}


