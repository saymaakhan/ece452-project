package com.example.ace.ui.camera

import com.example.ace.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.graphics.Canvas
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private val CLIPBOARD_SERVICE: String
        get() {
            TODO()
        }
    var clear: ImageView? = null
    var getImage: ImageView? = null
    var copy: ImageView? = null
    var recgText: EditText? = null
    var imageUri: Uri? = null
    var textRecognizer: TextRecognizer? = null
    var save_pdf: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        clear = findViewById(R.id.clear)
        getImage = findViewById(R.id.getImage)
        copy = findViewById(R.id.copy)
        recgText = findViewById(R.id.recgText)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        save_pdf = findViewById(R.id.pdf)


        val pdf = save_pdf
        if (pdf is Button){
            pdf.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@CameraActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is already granted, proceed with writing the PDF
                    createPDF()
                } else {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(
                        this@CameraActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
                    )
                }
            })

        }




        val image = getImage
        if (image is ImageView) {
            image.setOnClickListener(View.OnClickListener {
                ImagePicker.with(this@CameraActivity)
                    .crop() //Crop image(Optional), Check Customization for more option
                    .compress(1024) //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            })
        }
        val copyValue = copy
        if (copyValue is ImageView) {
            copyValue.setOnClickListener(View.OnClickListener {
                val textValue = recgText
                val text = textValue?.getText().toString()
                if (text.isEmpty()) {
                    Toast.makeText(this@CameraActivity, "there is no text to copy", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val clipboardManager =
                        getSystemService(this@CameraActivity.CLIPBOARD_SERVICE) as ClipboardManager
                    if (textValue is EditText) {
                        val clipData = ClipData.newPlainText("Data", textValue.getText().toString())

                    }
                    val clipData: ClipData = ClipData.newPlainText("", "")
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(this@CameraActivity, "Text copied to clipboard", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
        val clearValue = clear
        if (clearValue is ImageView) {
            clearValue.setOnClickListener(View.OnClickListener {
                val textValue = recgText
                val text = textValue?.getText().toString()
                if (text.isEmpty()) {
                    Toast.makeText(this@CameraActivity, "There is no text to clear", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (textValue is EditText) {
                        textValue.setText("")
                    }
                }
            })
        }
    }

    private fun createPDF() {
        save_pdf?.setOnClickListener(View.OnClickListener {
            val pdfdoc = PdfDocument()
            val paint = Paint()
            val pageinfo = PdfDocument.PageInfo.Builder(300,600,1).create()
            val page = pdfdoc.startPage(pageinfo)
            val canvas = page.canvas

            canvas.drawText("Canvas Text test", 40F, 50F, paint)
            pdfdoc.finishPage(page)

            val file = File(Environment.getExternalStorageDirectory(),"/ace_pdf.pdf")

            try {
                pdfdoc.writeTo(FileOutputStream(file))
                Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            pdfdoc.close()

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, you can now proceed with writing the PDF
                createPDF()
            } else {
                // Permission is denied, show a toast message to the user
                Toast.makeText(
                    this@CameraActivity,
                    "Permission to write to external storage is required to create the PDF",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                Toast.makeText(this, "image selected", Toast.LENGTH_SHORT).show()
                recognizeText()
            }
        } else {
            Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show()
        }
    }

//    fun createPDF(view: View){

//        val pdfdoc = PdfDocument()
//        val pageinfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
//        val page = pdfdoc.startPage(pageinfo)
//
//        val paint = Paint()
//        val str = recgText?.text?.toString()
//
//        val x = 10f
//        val y = 25f
//        if (str != null) {
//            page.canvas.drawText(str, x, y, paint)
//        }
//
//        pdfdoc.finishPage(page)
//
//        val filepath = Environment.getExternalStorageDirectory().path + "/ace_pdf.pdf"
//        val file = File(filepath)
//
//        try {
//            pdfdoc.writeTo(FileOutputStream(file))
//            Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception){
//            e.printStackTrace()
//            recgText?.setText("Error")
//        }
//        pdfdoc.close()

//        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
//        val file = File(pdfPath, "myPDF.pdf")
//        val outputStream: OutputStream = FileOutputStream(file)
//        val writer = PdfWriter(file)
//        val pdfDocument = PdfDocument(writer)
//        val document = Document(pdfDocument)
//
//    }



    private fun recognizeText() {
        if (imageUri != null) {
            try {
                val inputImage = InputImage.fromFilePath(this@CameraActivity, imageUri!!)
                val result = textRecognizer!!.process(inputImage)
                    .addOnSuccessListener { text ->
                        val recognizeText = text.text
                        recgText!!.setText(recognizeText)
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this@CameraActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}