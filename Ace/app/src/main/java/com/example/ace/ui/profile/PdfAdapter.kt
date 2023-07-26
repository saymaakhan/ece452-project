package com.example.ace.ui.profile

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R

class PdfAdapter(private val pdfUrls: List<String>) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_pdf, parent, false)
        return PdfViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdfUrl = pdfUrls[position]
        holder.bindPdf(pdfUrl)
    }

    override fun getItemCount(): Int = pdfUrls.size

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPdf(url: String) {
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }
    }
}
