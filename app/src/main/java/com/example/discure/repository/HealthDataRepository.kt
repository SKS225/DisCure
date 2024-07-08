package com.example.discure.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.discure.models.HealthData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HealthDataRepository {
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val healthDataList = MutableLiveData<List<HealthData>>()
    private val healthRef =
        FirebaseDatabase.getInstance().reference.child("users").child(userId).child("health_data")

    fun getHealthData(type: String): LiveData<List<HealthData>> {
        healthRef.child(type).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<HealthData>()
                for (dataSnapshot in snapshot.children) {
                    val date = dataSnapshot.child("date").getValue(String::class.java) ?: ""
                    val value = dataSnapshot.child("value").getValue(String::class.java) ?: ""
                    val healthData = HealthData(date, value)
                    //val healthData = snapshot.getValue(HealthData::class.java)
                    list.add(healthData)
                }
                healthDataList.value = list
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
        return healthDataList
    }

    fun saveHealthData(
        type: String,
        value: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val healthData = hashMapOf(
            "date" to currentDate,
            "value" to value
        )

        healthRef.child(type).push().setValue(healthData)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onError.invoke("Failed to save data: ${it.message}")
            }
    }
}
