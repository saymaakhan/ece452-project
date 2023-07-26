package com.example.ace.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import com.example.ace.ui.camera.PdfHandler
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R
import com.example.ace.data.Course
import com.example.ace.ui.grades.AddClassDialogFragment
import com.example.ace.ui.grades.AddGradesActivity
import com.example.ace.ui.grades.ClassInfo
import com.example.ace.ui.grades.UserInfo
import com.example.ace.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity(), AddCourseDialogFragment.OnAddClickListener {
    var DUMMY_COURSE_ID = "101"
    var DUMMY_COURSE_NAME = "Coding"
    private lateinit var containerClasses: LinearLayout
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tvCumulativeAverage: TextView
    private lateinit var tvNoClassesMessage: TextView
    private lateinit var pdfWebView: WebView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pdfHandler: PdfHandler
    private lateinit var pdfContainer: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        val nonNullUserId = userId ?: "defaultUserId"

        //FindViews
        containerClasses = findViewById(R.id.container_classes)
        val courseButton: ImageButton = findViewById(R.id.add_course_button)
        pdfHandler = PdfHandler()

        // Retrieve the pdfUrl from SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val pdfUrl = sharedPreferences.getString("pdfUrl", "")

        if (pdfUrl.isNullOrEmpty()) {
            // PDF not saved, show a message or handle this scenario
            //displayPdf(pdfUrl)
        } else {
            // Display the PDF using WebView
            updateUIWithPdfs(pdfUrl)
        }

        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(nonNullUserId)

        courseButton.setOnClickListener {
            // Show the AddClassDialogFragment
            val addClassDialog = AddCourseDialogFragment()
            addClassDialog.setOnAddClickListener(this)
            addClassDialog.show(supportFragmentManager, "AddClassDialogFragment")
        }

        fetchUserName(nonNullUserId)
        fetchUserEnrolledClasses(nonNullUserId)

    }

    private fun displayPdf(url: String?){
        if (url != null) {
            pdfWebView.settings.javaScriptEnabled = true
            pdfWebView.webViewClient = WebViewClient()
            pdfWebView.loadUrl(url)
            pdfWebView.visibility = View.VISIBLE
        }
    }

    private fun updateUIWithPdfs(pdfUrls: String) {
        pdfContainer = findViewById(R.id.pdfContainer)

        pdfContainer.removeAllViews()

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val pdfUrlList: List<String> = if (pdfUrls.isEmpty()) {
            emptyList() // If pdfUrls is empty, use an empty list
        } else {
            pdfUrls.split(",") // Assuming pdfUrls is a comma-separated string of URLs
        }

        val adapter = PdfAdapter(pdfUrlList)
        recyclerView.adapter = adapter
        pdfContainer.addView(recyclerView)

        if (pdfUrlList.isEmpty()) {
            // Show a message or handle the scenario when there are no PDFs
        } else {
            // Handle the case when there are PDFs
        }
    }



    private fun fetchUserName(userId: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userDocumentRef = firestore.collection("users").document(userId)
        userDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // User data exists, update the UI with the user's name
                    val userName = documentSnapshot.getString("displayName")
                    findViewById<TextView>(R.id.user_name).text = userName
                } else {
                    // Handle the case when user data does not exist in Firestore
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred while fetching user data
                Toast.makeText(this, "Error fetching user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun fetchUserEnrolledClasses(userId: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userDocumentRef = firestore.collection("classes").document(userId)
        val userClassesRef = userDocumentRef.collection("user_classes")

        // Get a reference to the user's document in the "user_enrolled_classes" collection

        userClassesRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // If there are enrolled classes, update the UI
                    val enrolledClassesList = mutableListOf<String>()
                    for (document in querySnapshot.documents) {
                        val className = document.id
                        enrolledClassesList.add(className)
                    }

                    // Update the UI with the enrolled classes
                    updateUIWithEnrolledClasses(enrolledClassesList)
                } else {
                    // If there are no enrolled classes, handle this case if needed
                    Toast.makeText(this, "Not currently enrolled in classes", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Error fetching enrolled classes
                Toast.makeText(this, "Error fetching enrolled classes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUIWithEnrolledClasses(enrolledClassesList: List<String>) {
        containerClasses = findViewById(R.id.container_classes)

        containerClasses.removeAllViews()

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = EnrolledClassesAdapter(enrolledClassesList)
        recyclerView.adapter = adapter
        containerClasses.addView(recyclerView)

        if (enrolledClassesList.isEmpty()) {
//            tvNoClassesMessage.visibility = View.VISIBLE
//            containerClasses.visibility = View.GONE
        } else {
//            tvNoClassesMessage.visibility = View.GONE
//            containerClasses.visibility = View.VISIBLE
        }



//TODO: FIX
//            tvNoClassesMessage.visibility = View.GONE
//            containerClasses.visibility = View.VISIBLE

    }

     override fun onAddClicked(className: String){

        val courseID = DUMMY_COURSE_ID
        val courseName = DUMMY_COURSE_NAME

        val course = Course(courseID, courseName)
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

         if (userId != null) {
             // Get a reference to the user's document in the "classes" collection
             val firestore = FirebaseFirestore.getInstance()
             val userDocumentRef = firestore.collection("classes").document(userId)

             // Create a new class document with a unique identifier (class name in this case)
             val classDocumentRef = userDocumentRef.collection("user_classes").document(className)

             // Create a ClassInfo object to store the class data
             val classInfo = ClassInfo(className)

             // Set the data of the new class document
             classDocumentRef.set(classInfo)
                 .addOnSuccessListener {
                     val classEntryView = layoutInflater.inflate(R.layout.class_item_layout, containerClasses, false)
                     classEntryView.findViewById<TextView>(R.id.tvClassName).text = className
//                     classEntryView.findViewById<TextView>(R.id.tvWeight).text = "Grade: --%"
//                     classEntryView.findViewById<ImageView>(R.id.letterGradeImageView).setImageResource(R.drawable.letter_unknown)

                     //containerClasses.addView(classEntryView)

//                     tvNoClassesMessage.visibility = View.GONE
                     containerClasses.visibility = View.VISIBLE

                     val userObject = UserInfo(userId)
                     firestore.collection("user_enrolled_classes").document(className).collection("users").document(userId).set(userObject)
                     firestore.collection("user_enrolled_classes").document(className).set(hashMapOf("test" to 1))
                     // Set an onClickListener for the class entry to open the GradesActivity
                     classEntryView.setOnClickListener {
                         // Launch the activity to add grades for the selected class
                         val intent = Intent(this, AddGradesActivity::class.java)
                         intent.putExtra("class_name", className)
                         startActivity(intent)
                     }

                     fetchUserEnrolledClasses(userId)

                     Toast.makeText(this, "Class saved successfully", Toast.LENGTH_SHORT).show()
                 }
                 .addOnFailureListener { exception ->
                     // Error saving class data
                     Toast.makeText(this, "Error saving class: ${exception.message}", Toast.LENGTH_SHORT).show()
                 }

         } else {
             // User is not logged in, handle this case if needed
         }

     }

}


/*
    private fun loadClassesFromFirestore() {

        // Get the user's UID
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user is logged in and the UID is not null
        if (userId != null) {
            // Get a reference to the user's document in the "classes" collection
            val firestore = FirebaseFirestore.getInstance()
            val userDocumentRef = firestore.collection("classes").document(userId)

            // Get all class documents from the subcollection
            userDocumentRef.collection("user_classes").get()
                .addOnSuccessListener { querySnapshot ->
                    // Clear existing views before displaying new classes
                    courseContainer.removeAllViews()

                    // Iterate through each class document in the subcollection
                    for (documentSnapshot in querySnapshot.documents) {
                        // Get the class data from the document
                        val classId = documentSnapshot.id
                        val className = documentSnapshot.getString("class_name")
                        // Add other properties as needed

                        // Create a view to display the class information
                        val classView = layoutInflater.inflate(R.layout.class_item_layout, null)
                        val classNameTextView = classView.findViewById<TextView>(R.id.classNameTextView)
                        classNameTextView.text = className

                        // Add the view to the container
                        courseContainer.addView(classView)

                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred while fetching data
                    Toast.makeText(this, "Error loading classes: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            // Error : User not logged in, send prompt.
            Toast.makeText(this, "Please log in to view your classes.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)
        }

    }
*/