package com.example.discure

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.discure.databinding.ActivityBmiResultBinding
import com.example.discure.models.BmiRecord
import com.example.discure.repository.BmiRepository
import com.example.discure.utilities.bmiCategory
import com.example.discure.utilities.setUpGauge
import com.example.discure.viewModelFactory.BmiCalculateViewModelFactory
import com.example.discure.viewModels.BmiCalculateViewModel

class BmiResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityBmiResultBinding
    private lateinit var viewModel: BmiCalculateViewModel
    var height: Float = 0.0f
    private var weight: Float = 0.0f
    private var bmi: Float = 0.0f
    private lateinit var gender: String
    private var minWeight: Float = 0.0f
    private var maxWeight: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiResultBinding.inflate(layoutInflater)
        val bmiRepository = BmiRepository()
        val factory = BmiCalculateViewModelFactory(bmiRepository)
        viewModel = ViewModelProvider(this, factory)[BmiCalculateViewModel::class.java]
        setContentView(binding.root)

        height = intent.getStringExtra("height")!!.toFloat()
        weight = intent.getStringExtra("weight")!!.toFloat()
        gender = intent.getStringExtra("gender")!!

        height /= 100
        bmi = weight / (height * height)
        bmi = "%.2f".format(bmi).toFloat()

        minWeight = "%.1f".format(18.5f * height * height).toFloat()
        maxWeight = "%.1f".format(24.9f * height * height).toFloat()

        binding.tvBmiCategory.text = "You are " + bmiCategory(bmi)
        binding.tvBmi.text = bmi.toString()
        binding.tvGender.text = gender
        binding.tvWeightRange.text = "Your ideal weight range is $minWeight - $maxWeight Kg."

        binding.btnRecalculateBmi.setOnClickListener {
            finish()
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val bmiRecord = BmiRecord(bmi, gender)
        viewModel.saveBmiRecord(bmiRecord)

        setUpGauge(binding.gaugeBmi, bmi)
    }
}