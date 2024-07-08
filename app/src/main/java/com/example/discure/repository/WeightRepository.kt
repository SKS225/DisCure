package com.example.discure.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WeightRepository {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val healthRef =
        userId?.let { FirebaseDatabase.getInstance().reference.child("users").child(it).child("health_data") }

    fun getLastEnteredWeight(onDataLoaded: (String) -> Unit, onError: (String) -> Unit) {
        healthRef?.child("Weight")
            ?.orderByChild("date")
            ?.limitToLast(1)

        healthRef?.child("Weight")?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val value = data.child("value").getValue(String::class.java) ?: ""
                        onDataLoaded(value)
                    }
                } else {
                    onError("No weight data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }


    fun saveGoalWeight(
        goalWeight: String,
        initialWeight: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val goalWeightData = hashMapOf("value" to goalWeight)
        val initialWeightData = hashMapOf("value" to initialWeight)

        healthRef?.child("goal_weight")
            ?.setValue(goalWeightData)
            ?.addOnSuccessListener {
                onSuccess.invoke()
            }
            ?.addOnFailureListener {
                onError.invoke("Failed to save goal weight: ${it.message}")
            }

        healthRef?.child("initial_weight")
            ?.setValue(initialWeightData)
            ?.addOnSuccessListener {
                onSuccess.invoke()
            }
            ?.addOnFailureListener {
                onError.invoke("Failed to save goal weight: ${it.message}")
            }
    }


    fun getGoalWeight(
        onSuccess1: (String) -> Unit,
        onSuccess2: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        healthRef?.child("goal_weight")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val goalWeight = snapshot.child("value").getValue(String::class.java)
                    if (goalWeight != null) {
                        onSuccess1.invoke(goalWeight)
                    } else {
                        onError.invoke("Goal weight data not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError.invoke("Failed to fetch goal weight: ${error.message}")
                }
            })

        healthRef?.child("initial_weight")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val initialWeight = snapshot.child("value").getValue(String::class.java)
                    if (initialWeight != null) {
                        onSuccess2.invoke(initialWeight)
                    } else {
                        onError.invoke("Goal weight data not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError.invoke("Failed to fetch goal weight: ${error.message}")
                }
            })
    }
}