package com.example.ace.ui.grades

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.ace.R

class AddItemGradeDialogFragment : DialogFragment() {

    interface OnSaveClickListener {
        fun onSaveClicked(itemName: String, gradeNumerator: String, gradeDenominator: String, percentage: String)
    }

    private lateinit var onSaveClickListener: OnSaveClickListener

    fun setOnSaveClickListener(listener: OnSaveClickListener) {
        onSaveClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_item_grade, null)

            builder.setView(view)
                .setPositiveButton("Save") { dialog, _ ->
                    val itemName = view.findViewById<EditText>(R.id.editItemName).text.toString()
                    val gradeNumerator = view.findViewById<EditText>(R.id.editNumerator).text.toString()
                    val gradeDenominator = view.findViewById<EditText>(R.id.editDenominator).text.toString()
                    val percentage = view.findViewById<EditText>(R.id.editPercentage).text.toString()
                    onSaveClickListener.onSaveClicked(itemName, gradeNumerator, gradeDenominator, percentage)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
