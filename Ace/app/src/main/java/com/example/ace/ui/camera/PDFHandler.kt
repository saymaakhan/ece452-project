package com.example.ace.ui.camera

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PdfHandler {

    private lateinit var pdfUrl: String

    fun setPdfUrl(url: String) {
        pdfUrl = url
    }

    fun getPdfUrl(): String {
        return pdfUrl
    }
}