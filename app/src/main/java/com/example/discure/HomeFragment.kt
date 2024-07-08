package com.example.discure

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.discure.databinding.FragmentHomeBinding
import com.example.discure.models.User
import com.example.discure.repository.BmiRepository
import com.example.discure.repository.WeightRepository
import com.example.discure.utilities.bmiCategory
import com.example.discure.utilities.setUpFullGauge
import com.example.discure.utilities.setUpGauge
import com.example.discure.utilities.updateWtCardText
import com.example.discure.viewModelFactory.HomeFragmentViewModelFactory
import com.example.discure.viewModels.HomeFragmentViewModel
import java.util.Calendar


class HomeFragment : Fragment() {
    //rulerview
    //link with google fit for steps count
    //use coroutines
    //disease predictor endless medical api cancel
    // send the medicines data to my another app
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var goalWeight: Int = 0
    private var initialWeight: Int = 0
    private var lastWeight: Int = 0
    private var glassCount: Int = 0
    private var glassCountMax: Int = 6
    private var workoutTime: Int = 0
    private var workoutTimeMax: Int = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val bmiRepository = BmiRepository()
        val weightRepository = WeightRepository()
        val factory = HomeFragmentViewModelFactory(bmiRepository, weightRepository)
        viewModel = ViewModelProvider(this, factory)[HomeFragmentViewModel::class.java]

        viewModel.user.observe(viewLifecycleOwner) {
            putValues(it)
        }

        viewModel.bmiRecordLiveData.observe(viewLifecycleOwner) { bmiRecord ->
            if (bmiRecord != null) {
                binding.tvBmi.text =
                    "Your BMI is ${bmiRecord.bmi} \nYou are ${bmiCategory(bmiRecord.bmi ?: 0f)}"
                setUpGauge(binding.gaugeCard1, bmiRecord.bmi!!)
            } else {
                binding.tvBmi.text = "BMI not found\nCalculate your BMI here!!"
                setOf(binding.gaugeCard1, 0f)
            }
        }

        viewModel.lastEnteredWeight.observe(viewLifecycleOwner) {
            val weight = it?.toIntOrNull() ?: 0  // Provide a default value if null
            binding.tvWeight.text = "$weight Kg"
            lastWeight = weight
            updateWtCardText(
                binding.gaugeCard2,
                binding.tvWeightGained,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }

        viewModel.goalWeight.observe(viewLifecycleOwner) {
            val goalWeightValue = it?.toIntOrNull() ?: 0  // Provide a default value if null
            goalWeight = goalWeightValue
            updateWtCardText(
                binding.gaugeCard2,
                binding.tvWeightGained,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }

        viewModel.initialWeight.observe(viewLifecycleOwner) {
            val initialWeightValue = it?.toIntOrNull() ?: 0  // Provide a default value if null
            initialWeight = initialWeightValue
            updateWtCardText(
                binding.gaugeCard2,
                binding.tvWeightGained,
                lastWeight,
                goalWeight,
                initialWeight
            )
        }


        updateWtCardText(
            binding.gaugeCard2,
            binding.tvWeightGained,
            lastWeight,
            goalWeight,
            initialWeight
        )


        binding.tvGreeting.text = getGreeting()

        binding.ivUser.setOnClickListener {
            val fragment = ProfileFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fl_home, fragment)
            transaction.commit()
        }

        binding.cvAlert.setOnClickListener {
            val intent = Intent(context, MedReminderActivity::class.java)
            startActivity(intent)
        }

        binding.card1.setOnClickListener {
            val intent = Intent(context, BmiCalculateActivity::class.java)
            startActivity(intent)
        }

        binding.card2.setOnClickListener {
            val intent = Intent(context, WeightTrackingActivity::class.java)
            startActivity(intent)
        }

        binding.card3.setOnClickListener {
            startActivity(Intent(context, WaterTrackingActivity::class.java))
        }

        binding.btnAdd1.setOnClickListener {
            glassCount++
            setUpFullGauge(binding.gaugeCard3, glassCount.toFloat(), glassCountMax.toFloat())
        }

        binding.btnLess1.setOnClickListener {
            glassCount--
            setUpFullGauge(binding.gaugeCard3, glassCount.toFloat(), glassCountMax.toFloat())
        }

        binding.card6.setOnClickListener {
            startActivity(Intent(context, MedReminderActivity::class.java))
        }

        binding.btnAdd2.setOnClickListener {
            workoutTime+=10
            setUpFullGauge(binding.gaugeCard4, workoutTime.toFloat(), workoutTimeMax.toFloat())
        }

        binding.btnLess2.setOnClickListener {
            workoutTime-=10
            setUpFullGauge(binding.gaugeCard4, workoutTime.toFloat(), workoutTimeMax.toFloat())
        }

        binding.card5.setOnClickListener {
            startActivity(Intent(context, SetGoalActivity::class.java))
        }

        binding.card6.setOnClickListener {
            startActivity(Intent(context, MedReminderActivity::class.java))
        }

        binding.card7.setOnClickListener {
            startActivity(Intent(context, BmiCalculateActivity::class.java))
        }

        binding.card8.setOnClickListener {
            startActivity(Intent(context,FatCalculateActivity::class.java))
        }

        return binding.root
    }

    private fun putValues(it: User) {
        binding.user = it
        Glide.with(this).load(it.photoUrl).into(binding.ivUser)
    }

    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        return when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}