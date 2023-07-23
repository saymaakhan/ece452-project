package com.example.ace.ui.camera

import com.example.ace.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {


    private val STORAGE_CODE = 1001
    lateinit var mStorage : StorageReference

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
    var btnGeneratePdf: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        clear = findViewById(R.id.clear)
        getImage = findViewById(R.id.getImage)
        copy = findViewById(R.id.copy)
        recgText = findViewById(R.id.recgText)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        btnGeneratePdf = findViewById(R.id.btn_generate_pdf)

        mStorage = FirebaseStorage.getInstance().reference
        val pdf = btnGeneratePdf


//        if(pdf is Button){
//            pdf.setOnClickListener{
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//                    if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_DENIED)
//                    {
//                        val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        requestPermissions(permission, STORAGE_CODE)
//                    } else {
//                        savePDF()
//                    }
//                } else {
//                    savePDF()
//                }
//            }
//
//        }

        if(pdf is Button){
            pdf.setOnClickListener{
                //Toast.makeText(this@CameraActivity, "Button Clicked", Toast.LENGTH_SHORT).show()
                val pdfByteArray = generatePdfByteArray()

                uploadPdfToFirebase(pdfByteArray)
            }
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

    private fun generatePdfByteArray(): ByteArray {
        val textValue = recgText
        val textContent = textValue?.text.toString()

        val outputStream = ByteArrayOutputStream()
        val document = Document()

        PdfWriter.getInstance(document, outputStream)

        document.open()
        document.add(Paragraph(textContent))
        document.close()

        return outputStream.toByteArray()

    }

    private fun uploadPdfToFirebase(pdfByteArray: ByteArray) {
        val pdfRef = mStorage.child("pdfs/ace.pdf")

        pdfRef.putBytes(pdfByteArray).addOnSuccessListener {
            Toast.makeText(this@CameraActivity, "File Upload Successful!" , Toast.LENGTH_SHORT).show()

            pdfRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val url = downloadUrl.toString()
                pasteDownloadLink(url)
            }

        }.addOnFailureListener{ e: Exception ->
            Toast.makeText(this@CameraActivity, ""+e.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    private fun pasteDownloadLink(url: String) {


    }


//    private fun savePDF() {
//        val textValue = recgText
//        //var mReference = mStorage.child("files")
//        val text = textValue?.getText().toString()
//        val mDoc = Document()
//        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
//            .format(System.currentTimeMillis())
//
//        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
//
//        try {
//            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
//            mDoc.open()
//
//            val data = text
//            mDoc.addAuthor("Ace_author")
//            mDoc.add(Paragraph(data))
//            mDoc.close()
//            Toast.makeText(this@CameraActivity, "$mFileName.pdf\n is created to \n $mFilePath", Toast.LENGTH_SHORT).show()
//
//        }catch (e: Exception){
//            Toast.makeText(this@CameraActivity, ""+e.toString(), Toast.LENGTH_SHORT).show()
//        }
//
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            STORAGE_CODE -> {
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    savePDF()
//                } else {
//                    Toast.makeText(this@CameraActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//    }

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