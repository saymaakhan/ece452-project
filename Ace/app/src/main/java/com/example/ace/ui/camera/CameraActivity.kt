package com.example.ace.ui.camera

import com.example.ace.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this@CameraActivity, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this@CameraActivity, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this@CameraActivity, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this@CameraActivity, R.anim.to_bottom_anim) }

    private var pdfUrl: String = ""

    private lateinit var builder: AlertDialog.Builder
    private lateinit var pdfHandler: PdfHandler
    private lateinit var  sharedPreferences: SharedPreferences


    lateinit var mStorage : StorageReference
//    var clear: ImageView? = null
//    var getImage: ImageView? = null
//    var copy: ImageView? = null
//    var recgText: EditText? = null
    var imageUri: Uri? = null
    var textRecognizer: TextRecognizer? = null
//    var btnGeneratePdf: Button? = null

    var textbox:EditText? = null
    var addBtn: FloatingActionButton? = null
    var copyBtn: FloatingActionButton? = null
    var pdfBtn: FloatingActionButton? = null
    var clearBtn: FloatingActionButton? = null
    var camBtn:FloatingActionButton? = null

    private var clicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
//        clear = findViewById(R.id.clear)
//        getImage = findViewById(R.id.getImage)
//        copy = findViewById(R.id.copy)
//        recgText = findViewById(R.id.recgText)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//        btnGeneratePdf = findViewById(R.id.btn_generate_pdf)

        addBtn = findViewById(R.id.plus)
        copyBtn = findViewById(R.id.copy)
        clearBtn = findViewById(R.id.clear)
        camBtn = findViewById(R.id.cam)
        pdfBtn = findViewById(R.id.pdf)
        textbox = findViewById(R.id.textbox)

        mStorage = FirebaseStorage.getInstance().reference
        builder = AlertDialog.Builder(this@CameraActivity)


        val add = addBtn
        if(add is FloatingActionButton){
            add.setOnClickListener {
                onAddBtnClicked()
            }
        }

        // <-------------------- Backend PDF Logic -------------------->
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        pdfHandler = PdfHandler()

        val pdf = pdfBtn
        if(pdf is FloatingActionButton){
            pdf.setOnClickListener{
                val textValue = textbox
                val textContent = textValue?.text.toString()

                if (textContent.isEmpty()){
                    Toast.makeText(this@CameraActivity, "Text box is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val pdfByteArray = generatePdfByteArray()
                    uploadPdfToFirebase(pdfByteArray)

//                    val editor = sharedPreferences.edit()
//                    editor.putString("pdfUrl", pdfUrl)
//                    editor.apply()

                    pdfHandler.setPdfUrl(pdfUrl)

                    builder.setTitle("PDF Created!")
                        .setMessage("Do you want to view the file?")
                        .setCancelable(true)
                        .setPositiveButton("Yes"){dialogInterface, it ->
                            //OPEN PDF
                            val url = pdfUrl
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                        .setNegativeButton("No"){dialogInterface, it ->
                            dialogInterface.cancel()
                        }
                        .show()
                }
            }
        }


        val image = camBtn
        if (image is FloatingActionButton) {
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
        val copyValue = copyBtn
        if (copyValue is FloatingActionButton) {
            copyValue.setOnClickListener(View.OnClickListener {
                val textValue = textbox
                val text = textValue?.getText().toString()
                if (text.isEmpty()) {
                    Toast.makeText(this@CameraActivity, "There is no text to copy", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    copyToClipboard(text)
                }
            })
        }
        val clearValue = clearBtn
        if (clearValue is FloatingActionButton) {
            clearValue.setOnClickListener(View.OnClickListener {
                val textValue = textbox
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

    private fun onAddBtnClicked() {
        setVisibility(clicked)
        setClickable(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            pdfBtn?.visibility ?: View.VISIBLE
            copyBtn?.visibility ?: View.VISIBLE
            clearBtn?.visibility ?: View.VISIBLE
            camBtn?.visibility ?: View.VISIBLE
        } else {
            pdfBtn?.visibility ?: View.INVISIBLE
            copyBtn?.visibility ?: View.INVISIBLE
            clearBtn?.visibility ?: View.INVISIBLE
            camBtn?.visibility ?: View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            pdfBtn?.startAnimation(fromBottom)
            copyBtn?.startAnimation(fromBottom)
            clearBtn?.startAnimation(fromBottom)
            camBtn?.startAnimation(fromBottom)
            addBtn?.startAnimation(rotateOpen)
        }else {
            pdfBtn?.startAnimation(toBottom)
            copyBtn?.startAnimation(toBottom)
            clearBtn?.startAnimation(toBottom)
            camBtn?.startAnimation(toBottom)
            addBtn?.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            pdfBtn?.isClickable = true
            copyBtn?.isClickable = true
            clearBtn?.isClickable = true
            camBtn?.isClickable = true
        } else {
            pdfBtn?.isClickable = false
            copyBtn?.isClickable = false
            clearBtn?.isClickable = false
            camBtn?.isClickable = false
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this@CameraActivity, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()

    }

    private fun generatePdfByteArray(): ByteArray {
        val textValue = textbox
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
        val textValue = textbox
        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())
        val websiteText = "Your PDF is ready!"
        val pdfRef = mStorage.child("pdfs/ace_" + mFileName + ".pdf")
        val spannableString = SpannableString(websiteText)



        pdfRef.putBytes(pdfByteArray).addOnSuccessListener {
            Toast.makeText(this@CameraActivity, "File Upload Successful!" , Toast.LENGTH_SHORT).show()

            pdfRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                pdfUrl = downloadUrl.toString()

                val editor = sharedPreferences.edit()
                editor.putString("pdfUrl",pdfUrl)
                editor.apply()
                //textValue?.setText(url)
//                val clickableSpan = object : ClickableSpan() {
//                    override fun onClick(view: View) {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                        startActivity(intent)
//                    }
//                }
//                spannableString.setSpan(clickableSpan, 0, websiteText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                val redColor = Color.RED
//                spannableString.setSpan(ForegroundColorSpan(redColor), 0, websiteText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                textValue?.text = spannableString.toEditable()
//                textValue?.movementMethod = LinkMovementMethod.getInstance()
            }

        }.addOnFailureListener{ e: Exception ->
            Toast.makeText(this@CameraActivity, ""+e.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    private fun SpannableString.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)



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
                        textbox!!.setText(recognizeText)
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