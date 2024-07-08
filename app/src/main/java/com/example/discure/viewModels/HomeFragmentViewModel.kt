package com.example.discure.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discure.models.BmiRecord
import com.example.discure.models.User
import com.example.discure.repository.BmiRepository
import com.example.discure.repository.WeightRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeFragmentViewModel(
    private val bmiRepository: BmiRepository,
    private val weightRepository: WeightRepository,
) : ViewModel() {
    init {
        getUser()
        fetchBmiData()
        getLastEnteredWeight()
        fetchGoalWeight()
    }
    val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    fun getUser() {
        viewModelScope.launch(Dispatchers.Main) { getUserData() }
    }

    private suspend fun getUserData() {
        val def = viewModelScope.async {
            Firebase.database.getReference("users").child(Firebase.auth.currentUser!!.uid).get()
                .await().getValue(User::class.java)
        }
        user.value = def.await()
    }

    /*fun observeUser(): LiveData<User> {
        return user
    }*/

    val bmiRecordLiveData: MutableLiveData<BmiRecord> by lazy { MutableLiveData<BmiRecord>() }

    fun fetchBmiData() {
        bmiRepository.getBmiRecord { bmiRecord ->
            bmiRecordLiveData.postValue(bmiRecord)
        }
    }

    /*fun observeBmiData(): LiveData<BmiRecord> {
        return bmiRecordLiveData
    }*/


    private val _lastEnteredWeight = MutableLiveData<String>()
    val lastEnteredWeight: LiveData<String>
        get() = _lastEnteredWeight

    fun getLastEnteredWeight() {
        weightRepository.getLastEnteredWeight(onDataLoaded = { lastWeight ->
            _lastEnteredWeight.value = lastWeight
        }, onError = { errorMessage ->
            Log.e("HealthTrackViewModel", "Error fetching last weight: $errorMessage")
        })
    }

    private val _goalWeight = MutableLiveData<String>()
    val goalWeight: LiveData<String>
        get() = _goalWeight

    private val _initialWeight = MutableLiveData<String>()
    val initialWeight: LiveData<String>
        get() = _initialWeight

    fun fetchGoalWeight() {
        weightRepository.getGoalWeight(onSuccess1 = { goalWeight ->
            _goalWeight.value = goalWeight
        }, onSuccess2 = { initialWeight ->
            _initialWeight.value = initialWeight
        }, onError = { error ->
            Log.e("WeightTrackViewModel", "Error fetching goal weight: $error")
        })
    }
}