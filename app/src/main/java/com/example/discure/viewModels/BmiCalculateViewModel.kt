package com.example.discure.viewModels

import androidx.lifecycle.ViewModel
import com.example.discure.models.BmiRecord
import com.example.discure.repository.BmiRepository

class BmiCalculateViewModel(private val bmiRepository: BmiRepository): ViewModel() {
    fun saveBmiRecord(bmiRecord: BmiRecord) {
        bmiRepository.saveBmiRecord(bmiRecord)
    }
}