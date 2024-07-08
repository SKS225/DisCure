package com.example.discure.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.discure.models.HealthData
import com.example.discure.repository.HealthDataRepository

class HealthTrackViewModel(private val repository: HealthDataRepository) : ViewModel() {

    fun getHealthData(type: String): LiveData<List<HealthData>> {
        return repository.getHealthData(type)
    }

    fun saveHealthData(
        type: String,
        value: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.saveHealthData(type, value, onSuccess, onError)
    }
}
