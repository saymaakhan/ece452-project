package com.example.ace.ui.camera

import com.example.ace.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class CameraActivity : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        clear = findViewById(R.id.clear)
        getImage = findViewById(R.id.getImage)
        copy = findViewById(R.id.copy)
        recgText = findViewById(R.id.recgText)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
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