package com.example.ace.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.ace.R

class EditNameDialogFragment : DialogFragment() {

    // Interface to communicate changes to the calling activity
    interface OnNameUpdateListener {
        fun onNameUpdated(newName: String)
    }

    private lateinit var newNameEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_name_dialog, container, false)
        newNameEditText = view.findViewById(R.id.newNameEditText)

        // Set the initial value of the EditText to the current user name
        val currentName = arguments?.getString("currentName")
        newNameEditText.setText(currentName)

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            val newName = newNameEditText.text.toString().trim()
            if (newName.isNotEmpty()) {
                // Communicate the changes to the calling activity
                val listener = activity as? OnNameUpdateListener
                listener?.onNameUpdated(newName)
                dismiss()
            }
        }

        return view
    }
}
