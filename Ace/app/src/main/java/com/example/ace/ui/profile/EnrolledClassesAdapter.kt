package com.example.ace.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.R
import com.example.ace.ui.grades.AddGradesActivity

class EnrolledClassesAdapter(private val enrolledClassesList: List<String>) : RecyclerView.Adapter<EnrolledClassesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvClassName: TextView = itemView.findViewById(R.id.tvClassName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val className = enrolledClassesList[position]
        holder.tvClassName.text = className

        holder.itemView.setOnClickListener {
            // TODO: Open GradeActivity or Change to something else
            val context = holder.itemView.context
            val intent = Intent(context, AddGradesActivity::class.java)
            intent.putExtra("class_name", className)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return enrolledClassesList.size
    }
}

