package com.example.discure

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class TabAddDialogFragment : DialogFragment() {

    interface OnTabAddedListener {
        fun onTabAdded(type: String)
    }

    private var listener: OnTabAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnTabAddedListener) {
            context
        } else if (targetFragment is OnTabAddedListener) {
            targetFragment as OnTabAddedListener
        } else {
            throw RuntimeException("$context must implement OnTabAddedListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputField = EditText(requireContext())
        inputField.hint = "Enter tab type"
        Log.d("TabAddDialogFragment", "Dialog created")

        return AlertDialog.Builder(requireContext())
            .setTitle("Add New Tab")
            .setView(inputField)
            .setPositiveButton("Add") { _, _ ->
                val type = inputField.text.toString().trim()
                if (type.isNotEmpty()) {
                    listener?.onTabAdded(type)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}