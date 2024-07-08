package com.example.discure.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discure.repository.HealthDataRepository
import com.example.discure.viewModels.HealthTrackViewModel

class HealthTrackViewModelFactory(private val healthRepository: HealthDataRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthTrackViewModel::class.java)) {
            return HealthTrackViewModel(healthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
