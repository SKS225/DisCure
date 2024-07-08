package com.example.discure.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discure.models.HealthData
import com.example.discure.repository.HealthDataRepository
import com.example.discure.repository.WeightRepository

class WeightTrackViewModel(private val weightRepository: WeightRepository, private val healthRepository: HealthDataRepository) : ViewModel() {
    init {
        fetchGoalWeight()
        getLastEnteredWeight()
    }

    fun saveGoalWeight(
        goalWeight: String,
        initialWeight: String = lastEnteredWeight.value.toString()
    ) {
        weightRepository.saveGoalWeight(goalWeight, initialWeight,
            onSuccess = {
                //_saveGoalWeightResult.value = Event("Goal weight saved successfully")
            },
            onError = {
                //_saveGoalWeightResult.value = Event("Error: $error")
            }
        )
    }

    private val _lastEnteredWeight = MutableLiveData<String>()
    val lastEnteredWeight: LiveData<String>
        get() = _lastEnteredWeight

    fun getLastEnteredWeight() {
        weightRepository.getLastEnteredWeight(
            onDataLoaded = { lastWeight ->
                _lastEnteredWeight.value = lastWeight
            },
            onError = { errorMessage ->
                Log.e("HealthTrackViewModel", "Error fetching last weight: $errorMessage")
            }
        )
    }

    private val _goalWeight = MutableLiveData<String>()
    val goalWeight: LiveData<String>
        get() = _goalWeight

    private val _initialWeight = MutableLiveData<String>()
    val initialWeight: LiveData<String>
        get() = _initialWeight

    fun fetchGoalWeight() {
        weightRepository.getGoalWeight(
            onSuccess1 = { goalWeight ->
                _goalWeight.value = goalWeight
            }, onSuccess2 = { initialWeight ->
                _initialWeight.value = initialWeight
            },
            onError = { error ->
                Log.e("WeightTrackViewModel", "Error fetching goal weight: $error")
            }
        )
    }

    fun getHealthData(type: String): LiveData<List<HealthData>> {
        return healthRepository.getHealthData(type)
    }

    fun saveHealthData(
        type: String,
        value: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        healthRepository.saveHealthData(type, value, onSuccess, onError)
    }
}