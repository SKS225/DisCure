package com.example.discure

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.discure.databinding.ActivityCalculateBmiBinding


class BmiCalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateBmiBinding

    private var weight: Int = 0
    private var age: Int = 0
    var currentProgress: Int = 0
    private var gender: String? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculateBmiBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.rlMale.setOnClickListener {
            binding.rlMale.background = AppCompatResources.getDrawable(applicationContext,R.drawable.malefemalefocus)
            binding.rlFemale.background = AppCompatResources.getDrawable(applicationContext,R.drawable.malefemalenotfocus)
            gender="Male";
        }

        binding.rlFemale.setOnClickListener{
            binding.rlFemale.background = ContextCompat.getDrawable(applicationContext, R.drawable.malefemalefocus )
            binding.rlMale.background = AppCompatResources.getDrawable(applicationContext, R.drawable.malefemalenotfocus)
            gender = "Female"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.sbHeight.max = 300
        binding.sbHeight.progress = 170
        binding.sbHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentProgress = progress
                binding.tvCurrHeight.text = currentProgress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {   }
            override fun onStopTrackingTouch(seekBar: SeekBar) {    }
        })

        binding.npWeight.minValue = 30
        binding.npWeight.maxValue = 150
        binding.npWeight.textColor = Color.WHITE
        binding.npWeight.wrapSelectorWheel = false
        binding.npWeight.value = 55
        binding.npWeight.setOnValueChangedListener { _, _, newVal ->
            weight = newVal
        }

        binding.npAge.minValue = 10
        binding.npAge.maxValue = 100
        binding.npAge.textColor = Color.WHITE
        binding.npAge.wrapSelectorWheel = false
        binding.npAge.value = 22
        binding.npAge.setOnValueChangedListener { _, _, newVal ->
            age = newVal
        }

        binding.btnCalculateBmi.setOnClickListener{
            if(gender == "")
                    Toast.makeText(applicationContext,"Select Your Gender First",Toast.LENGTH_SHORT).show()

            else if(currentProgress.toString() == "0")
                Toast.makeText(applicationContext,"Select Your Height First",Toast.LENGTH_SHORT).show()

            else if(age==0 || age<0)
                Toast.makeText(applicationContext,"Age is Incorrect",Toast.LENGTH_SHORT).show()

            else if(weight==0|| weight<0)
                Toast.makeText(applicationContext,"Weight Is Incorrect",Toast.LENGTH_SHORT).show()

            else {
                val intent = Intent(this,BmiResultActivity::class.java)
                intent.putExtra("gender", gender)
                intent.putExtra("height", currentProgress.toString())
                intent.putExtra("weight", weight.toString())
                startActivity(intent)
            }
        }
    }
}
