package com.example.discure.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discure.repository.HealthDataRepository
import com.example.discure.repository.WeightRepository
import com.example.discure.viewModels.WeightTrackViewModel

class WeightTrackViewModelFactory(
    private val healthRepository: HealthDataRepository,
    private val weightRepository: WeightRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightTrackViewModel::class.java)) {
            return WeightTrackViewModel(weightRepository, healthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
