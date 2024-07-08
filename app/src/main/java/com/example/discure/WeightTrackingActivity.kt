package com.example.discure

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discure.adapters.HealthDataAdapter
import com.example.discure.databinding.ActivityWeightTrackingBinding
import com.example.discure.models.HealthData
import com.example.discure.repository.HealthDataRepository
import com.example.discure.repository.WeightRepository
import com.example.discure.utilities.displayLineChart
import com.example.discure.utilities.setupChart
import com.example.discure.utilities.updateWtCardText
import com.example.discure.viewModelFactory.WeightTrackViewModelFactory
import com.example.discure.viewModels.WeightTrackViewModel

class WeightTrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeightTrackingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var healthDataAdapter: HealthDataAdapter
    private lateinit var healthDataRepository: HealthDataRepository
    private lateinit var healthDataList: List<HealthData>
    private lateinit var viewModel: WeightTrackViewModel


    /*private val factory = ViewModelProvider.Factory { modelClass ->
        if (modelClass.isAssignableFrom(HealthTrackViewModel::class.java)) {
            HealthTrackViewModel(healthDataRepository) // Pass the dependency here
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/

    /*private val viewModel: WeightTrackViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WeightTrackViewModel::class.java)) {
                    return WeightTrackViewModel(WeightRepository()) as T // Pass the dependency here
                } else {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }*/

    //private val viewModel: HealthTrackViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeightTrackingBinding.inflate(layoutInflater)
        val healthRepository = HealthDataRepository()
        val weightRepository = WeightRepository()
        val factory = WeightTrackViewModelFactory(healthRepository, weightRepository)
        viewModel = ViewModelProvider(this, factory)[WeightTrackViewModel::class.java]
        setContentView(binding.root)

        val bottomSheetDialogFragment = HealthAddFragment()
        val type = "Weight"
        //dropdownSpinner = findViewById(R.id.spinner_more)
        healthDataRepository = HealthDataRepository()

        setupChart(binding.lineChart)
        showWtCard()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddWeight.setOnClickListener {
            bottomSheetDialogFragment.show(
                supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }

        recyclerView = binding.rvHistory
        recyclerView.layoutManager = LinearLayoutManager(this)
        healthDataAdapter = HealthDataAdapter(emptyList()) { healthData, moreButton ->
            showPopupMenu(healthData, moreButton!!)
        }
        recyclerView.adapter = healthDataAdapter

        viewModel.getHealthData(type).observe(this) { data ->
            healthDataList = data
            healthDataAdapter.setData(healthDataList)
            displayLineChart(binding.lineChart, healthDataList)
        }

        /*moreButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.rv_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> editValue()
                    R.id.remove -> removeValue()
                }
                true
            }
            popupMenu.show()
        }*/
    }

    private fun removeValue(healthData: HealthData) {
        Toast.makeText(this, "Remove", Toast.LENGTH_SHORT).show()
    }

    private fun editValue(healthData: HealthData) {
        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
    }

    private fun showPopupMenu(healthData: HealthData, moreButton: ImageButton){
        val popupMenu = PopupMenu(this, moreButton)
        popupMenu.menuInflater.inflate(R.menu.rv_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> editValue(healthData)
                R.id.remove -> removeValue(healthData)
            }
            true
        }
        popupMenu.show()
    }



    private fun showWtCard() {
        var lastWeight = 0
        var goalWeight = 0
        var initialWeight = 0

        binding.cvWeightGoal.visibility = View.VISIBLE

        binding.btnEditGoal.setOnClickListener {
            val inputDialog = SetGoalDialogFragment()
            inputDialog.setOnInputSubmittedListener { enteredWeight ->
                viewModel.saveGoalWeight(enteredWeight)
            }
            inputDialog.show(supportFragmentManager, "InputDialog")
        }

        viewModel.goalWeight.observe(this) {
            val goalWeightValue = it?.toIntOrNull() ?: 0  // Provide a default value if null
            goalWeight = goalWeightValue
            updateWtCardText(
                binding.gaugeWeightGoal,
                binding.tvWeightGoal,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }

        viewModel.initialWeight.observe(this) {
            val initialWeightValue = it?.toIntOrNull() ?: 0  // Provide a default value if null
            initialWeight = initialWeightValue
            updateWtCardText(
                binding.gaugeWeightGoal,
                binding.tvWeightGoal,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }

        viewModel.lastEnteredWeight.observe(this) {
            val weight = it?.toIntOrNull() ?: 0
            lastWeight = weight
            updateWtCardText(
                binding.gaugeWeightGoal,
                binding.tvWeightGoal,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }

        viewModel.getLastEnteredWeight()
        viewModel.fetchGoalWeight()

        updateWtCardText(
            binding.gaugeWeightGoal,
            binding.tvWeightGoal,
            lastWeight,
            goalWeight,
            initialWeight
        )
    }
}