package com.example.ace.ui.profile

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.ace.R
import com.google.android.material.textfield.TextInputLayout

class AddCourseDialogFragment : DialogFragment() {

    private var onAddClickListener: OnAddClickListener? = null

    interface OnAddClickListener {
        fun onAddClicked(courseName: String)
    }

    fun setOnAddClickListener(listener: ProfileActivity) {
        onAddClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_class, null)
            val textInputLayoutCourseName = view.findViewById<TextInputLayout>(R.id.textInputLayoutClassName)

            builder.setView(view)
                .setTitle("Add Course")
                .setPositiveButton("Save") { _, _ ->
                    val courseName = textInputLayoutCourseName.editText?.text.toString().trim()
                    onAddClickListener?.onAddClicked(courseName)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Cancel the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
