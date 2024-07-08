package com.example.discure.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discure.repository.MedReminderRepository
import com.example.discure.viewModels.MedReminderViewModel

class MedReminderViewModelFactory(private val repository: MedReminderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedReminderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
