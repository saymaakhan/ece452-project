package com.example.ace.ui.grades

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.ace.R

class AddSyllabusItemDialogFragment : DialogFragment() {

    interface OnSaveClickListener {
        fun onSaveClicked(itemName: String, itemWeight: String)
    }

    private lateinit var onSaveClickListener: OnSaveClickListener

    fun setOnSaveClickListener(listener: OnSaveClickListener) {
        onSaveClickListener = listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_add_syllabus_item, null)

        val editItemName = dialogView.findViewById<EditText>(R.id.editItemName)
        val editItemWeight = dialogView.findViewById<EditText>(R.id.editItemWeight)

        builder.setView(dialogView)
            .setTitle("Add Syllabus Item")
            .setPositiveButton("Save") { _, _ ->
                val itemName = editItemName.text.toString().trim()
                val itemWeight = editItemWeight.text.toString().trim()
                onSaveClickListener.onSaveClicked(itemName, itemWeight)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }
}
