package com.example.discure.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discure.repository.BmiRepository
import com.example.discure.repository.WeightRepository
import com.example.discure.viewModels.HomeFragmentViewModel

class HomeFragmentViewModelFactory(
    private val bmiRepository: BmiRepository,
    private val weightRepository: WeightRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            return HomeFragmentViewModel(bmiRepository, weightRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
