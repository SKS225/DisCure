package com.example.discure

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.discure.databinding.ActivitySetGoalBinding

class SetGoalActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetGoalBinding
    private var weight: Int = 50  // Initial value to match the layout
    private var water: Int = 6    // Initial value to match the layout
    private var workoutTime: Int = 30 // Initial value to match the layout
    private var steps: Int = 5000 // Initial value to match the layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial values
        binding.etWeight.setText(weight.toString())
        binding.etWater.setText(water.toString())
        binding.etTime.setText(workoutTime.toString())
        binding.etSteps.setText(steps.toString())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnWeightInc.setOnClickListener {
            weight++
            binding.etWeight.setText(weight.toString())
        }
        binding.btnWeightDec.setOnClickListener {
            if (weight > 0) {
                weight--
                binding.etWeight.setText(weight.toString())
            }
        }

        binding.btnGlassInc.setOnClickListener {
            water++
            binding.etWater.setText(water.toString())
        }
        binding.btnGlassDec.setOnClickListener {
            if (water > 0) {
                water--
                binding.etWater.setText(water.toString())
            }
        }

        binding.btnTimeInc.setOnClickListener {
            workoutTime += 10
            binding.etTime.setText(workoutTime.toString())
        }

        binding.btnTimeDec.setOnClickListener {
            if (workoutTime >= 10) {
                workoutTime -= 10
                binding.etTime.setText(workoutTime.toString())
            }
        }

        binding.btnStepsInc.setOnClickListener {
            steps += 1000
            binding.etSteps.setText(steps.toString())
        }

        binding.btnStepsDec.setOnClickListener {
            if (steps >= 1000) {
                steps -= 1000
                binding.etSteps.setText(steps.toString())
            }
        }

        binding.btnSave.setOnClickListener {
            // Handle save action
        }
    }
}
