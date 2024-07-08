package com.example.discure.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discure.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel: ViewModel() {
    init {
        getUser()
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
}