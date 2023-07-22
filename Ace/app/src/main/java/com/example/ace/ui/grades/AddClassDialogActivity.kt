package com.example.ace.ui.grades

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.ace.R
import com.google.android.material.textfield.TextInputLayout

class AddClassDialogFragment : DialogFragment() {

    private var onSaveClickListener: OnSaveClickListener? = null

    interface OnSaveClickListener {
        fun onSaveClicked(className: String)
    }

    fun setOnSaveClickListener(listener: GradesActivity) {
        onSaveClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_class, null)
            val textInputLayoutClassName = view.findViewById<TextInputLayout>(R.id.textInputLayoutClassName)

            builder.setView(view)
                .setTitle("Add Class")
                .setPositiveButton("Save") { _, _ ->
                    val className = textInputLayoutClassName.editText?.text.toString().trim()
                    onSaveClickListener?.onSaveClicked(className)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Cancel the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
