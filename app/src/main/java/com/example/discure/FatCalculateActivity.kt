package com.example.discure

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.discure.databinding.ActivityFatCalculateBinding
import com.google.android.material.tabs.TabLayout

class FatCalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFatCalculateBinding
    private var gender:String = "Male"
    private var height:Float = 170f
    private var weight:Int = 60
    private var neck:Float = 0f
    private var waist:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        binding = ActivityFatCalculateBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tabLayoutGender.setOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    gender = if (tab.position == 0)
                        "Male"
                    else
                        "Female"
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabReselected(p0: TabLayout.Tab?) {
                }
            }
        )

        binding.ruleViewHeight.setOnValueChangedListener {
            height = it
        }

        binding.npWeight.minValue = 30
        binding.npWeight.maxValue = 150
        binding.npWeight.wrapSelectorWheel = false
        binding.npWeight.value = 55
        binding.npWeight.setOnValueChangedListener { _, _, newVal ->
            weight = newVal
        }

        binding.rulerViewNeck.setUnitStr("in")
        binding.rulerViewNeck.setValueListener {
            neck = binding.rulerViewNeck.getValue() // get the rule current value
        }

        binding.rulerViewWaist.setValueListener {
           waist = it
        }

        binding.btnCalculateBmi.setOnClickListener {
            binding.clFatResult.visibility = View.VISIBLE
            Toast.makeText(this, "$gender $height cm $weight Kg $neck in $waist in", Toast.LENGTH_SHORT).show()
        }
    }
}