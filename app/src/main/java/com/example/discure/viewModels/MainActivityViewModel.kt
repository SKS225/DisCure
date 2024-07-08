package com.example.discure.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discure.models.BmiRecord
import com.example.discure.models.User
import com.example.discure.repository.BmiRepository
import com.example.discure.repository.HealthDataRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*class MainActivityViewModel: ViewModel() {
    private val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val bmiRecordLiveData: MutableLiveData<BmiRecord> by lazy { MutableLiveData<BmiRecord>() }
    private val repository = UserRepository()
    private val repo2 = HealthDataRepository()

    fun getData(){
        viewModelScope.launch(Dispatchers.Main){ getUser() }
    }

    private suspend fun getUser() {
        val def = viewModelScope.async {
            Firebase.database.getReference("users").child(Firebase.auth.currentUser!!.uid).get()
                .await().getValue(User::class.java)
        }
        user.value = def.await()
    }

    fun observeUser(): LiveData<User> {
        return user
    }

    fun fetchBmiData() {
        repository.getLatestBmiRecord { bmiRecord ->
            bmiRecordLiveData.postValue(bmiRecord)
        }
    }

    fun saveBmiRecord(bmiRecord: BmiRecord) {
        BmiRepository.saveBmiRecord(bmiRecord)
    }

    fun observeBmiData(): LiveData<BmiRecord> {
        return bmiRecordLiveData
    }

    private val _lastEnteredWeight = MutableLiveData<String>()
    val lastEnteredWeight: LiveData<String>
        get() = _lastEnteredWeight

    fun fetchLastEnteredWeight() {
        repo2.getLastEnteredWeight(
            onDataLoaded = { lastWeight ->
                _lastEnteredWeight.postValue(lastWeight)
            },
            onError = { error ->
                // Handle error, e.g., show toast or log error
                Log.e("HealthTrackViewModel", "Error fetching last entered weight: $error")
            }
        )
    }
}*/