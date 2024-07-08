// SetGoalDialogFragment.kt
package com.example.discure

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SetGoalDialogFragment : DialogFragment() {

    private var onInputSubmittedListener: ((String) -> Unit)? = null
    private val initialWeight: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_input_dialog, null)

        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.np_dialog)

        numberPicker.minValue = 0
        numberPicker.maxValue = 150
        numberPicker.wrapSelectorWheel = false
        numberPicker.value = initialWeight


        builder.setView(dialogView)
            .setTitle("Set Target Weight")
            .setPositiveButton("OK") { dialog, _ ->
                val selectedValue = numberPicker.value
                onInputSubmittedListener?.invoke(selectedValue.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    fun setOnInputSubmittedListener(listener: (String) -> Unit) {
        this.onInputSubmittedListener = listener
    }
}
