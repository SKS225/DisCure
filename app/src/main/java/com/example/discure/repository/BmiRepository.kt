package com.example.discure.repository

import com.example.discure.models.BmiRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BmiRepository {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val bmiRef = userId?.let {
        FirebaseDatabase.getInstance().reference
        .child("users")
        .child(it)
        .child("health_data")
        .child("bmi")
    }

    fun saveBmiRecord(bmiRecord: BmiRecord) {
        bmiRef?.setValue(bmiRecord)
    }

    fun getBmiRecord(callback: (BmiRecord?) -> Unit) {
        bmiRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bmiRecord = snapshot.getValue(BmiRecord::class.java)
                callback.invoke(bmiRecord)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.invoke(null)
            }
        })
    }
}
