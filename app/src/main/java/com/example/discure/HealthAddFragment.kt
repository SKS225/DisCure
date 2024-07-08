package com.example.discure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discure.databinding.FragmentAddHealthBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.discure.repository.HealthDataRepository
import androidx.fragment.app.viewModels
import com.example.discure.viewModelFactory.HealthTrackViewModelFactory
import com.example.discure.viewModels.HealthTrackViewModel
import com.google.android.gms.common.ErrorDialogFragment
import com.google.android.material.snackbar.Snackbar

class HealthAddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddHealthBinding
    //val healthDataRepository = HealthDataRepository()
    private lateinit var viewModel: HealthTrackViewModel
    //private var dataType: String = "weight" // Default type



    /*private val viewModel: HealthTrackViewModel by viewModels { factory }

    private val factory = object : ViewModelProvider.Factory { // Implement the ViewModelProvider.Factory interface
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HealthTrackViewModel::class.java)) {
                // Pass any dependencies to the ViewModel constructor here (e.g., repository)
                @Suppress("UNCHECKED_CAST")
                return HealthTrackViewModel(healthDataRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val healthRepository = HealthDataRepository()
        val factory = HealthTrackViewModelFactory(healthRepository)
        viewModel = ViewModelProvider(this, factory)[HealthTrackViewModel::class.java]

        binding.addHealthButton.setOnClickListener {
            val value = binding.etAddHealth.text.toString().trim()

            if (value.isEmpty()) {
                Snackbar.make(view, "Please enter a value first", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = "Weight"
            viewModel.saveHealthData(type, value,
                onSuccess = {
                    dismiss()
                },
                onError = { errorMessage ->
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                })
        }
    }
}
