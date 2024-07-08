package com.example.discure

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discure.adapters.HealthDataAdapter
import com.example.discure.databinding.FragmentTrackHealthBinding
import com.example.discure.models.HealthData
import com.example.discure.repository.HealthDataRepository
import com.example.discure.repository.WeightRepository
import com.example.discure.utilities.displayLineChart
import com.example.discure.utilities.setupChart
import com.example.discure.viewModelFactory.HealthTrackViewModelFactory
import com.example.discure.viewModelFactory.WeightTrackViewModelFactory
import com.example.discure.viewModels.HealthTrackViewModel
import com.example.discure.viewModels.WeightTrackViewModel
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HealthTrackFragment : Fragment(), TabAddDialogFragment.OnTabAddedListener {

    private lateinit var binding: FragmentTrackHealthBinding
    private var healthType: String = "Weight"
    private lateinit var recyclerView: RecyclerView
    private lateinit var healthDataAdapter: HealthDataAdapter
    private lateinit var healthDataList: List<HealthData>
    private lateinit var viewModel: HealthTrackViewModel
    private lateinit var types: MutableList<String>
    private var type: String = "Weight"


    /*private val factory = ViewModelProvider.Factory { modelClass ->
        if (modelClass.isAssignableFrom(HealthTrackViewModel::class.java)) {
            HealthTrackViewModel(healthDataRepository) // Pass the dependency here
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/

    /*private val viewModel: HealthTrackViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HealthTrackViewModel::class.java)) {
                    return HealthTrackViewModel(healthDataRepository) as T // Pass the dependency here
                } else {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }*/

    companion object {
        private const val ARG_HEALTH_TYPE = "health_type"

        fun newInstance(healthType: String): HealthTrackFragment {
            val fragment = HealthTrackFragment()
            val args = Bundle()
            args.putString(ARG_HEALTH_TYPE, healthType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackHealthBinding.inflate(inflater, container, false)
        val healthRepository = HealthDataRepository()
        val factory = HealthTrackViewModelFactory(healthRepository)
        viewModel = ViewModelProvider(this, factory)[HealthTrackViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        healthType = arguments?.getString(ARG_HEALTH_TYPE, "") ?: ""

        val bottomSheetDialogFragment = HealthAddFragment()
        //HealthTrackActivity.type = intent.getStringExtra("type")!!

        types = mutableListOf("Weight", "Blood Pressure", "Cholestrol", "Sugar Level", "Add")


        val healthRepository = HealthDataRepository()
        val factory = HealthTrackViewModelFactory(healthRepository)
        viewModel = ViewModelProvider(this, factory)[HealthTrackViewModel::class.java]


        //viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HealthTrackViewModel::class.java]


        setupChart(binding.chart1)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTrackHealth.adapter = spinnerAdapter

        // ... (rest of you

        //setContentView(binding.root)

        binding.btnAddHealth.setOnClickListener {
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
        }

        //val type = intent.getStringExtra("type") ?: ""
        binding.tvTrackHealth.text = "Track $healthType"

        recyclerView = binding.rvHistory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        healthDataAdapter = HealthDataAdapter(emptyList()) { healthData,moreButton ->
            showPopupMenu(healthData,moreButton!!)
        }
        recyclerView.adapter = healthDataAdapter

        viewModel.getHealthData(type).observe(viewLifecycleOwner) { data ->
            healthDataList = data
            healthDataAdapter.setData(healthDataList)
            displayLineChart(binding.chart1, healthDataList)
        }

        binding.spinnerTrackHealth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == types.size - 1) {
                    showAddTabDialog()
                    //Toast.makeText(context, "Add clicked", Toast.LENGTH_SHORT).show()
                } else {
                    type = types[position]
                    viewModel.getHealthData(type).observe(viewLifecycleOwner) { data ->
                        healthDataList = data
                        healthDataAdapter.setData(healthDataList)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

        }
    }

    override fun onTabAdded(type: String) {
        types.add(types.size - 1, type)
        this.type = type
        viewModel.getHealthData(type).observe(viewLifecycleOwner) { data ->
            healthDataList = data
            healthDataAdapter.setData(healthDataList)
        }
        //tabTitles.add(tabTitles.size - 1, type)
        //tabFragments.add(tabTitles.size - 1, HealthTrackFragment.newInstance(type))
        //viewPager.adapter?.notifyDataSetChanged()
        //tabLayout.getTabAt(tabTitles.size - 2)?.select()
    }

    private fun removeValue(healthData: HealthData) {
        Toast.makeText(context, "Remove clicked", Toast.LENGTH_SHORT).show()
    }

    private fun editValue(healthData: HealthData) {
        Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show()
    }

    private fun showPopupMenu(healthData: HealthData,moreButton:ImageButton){
        val popupMenu = PopupMenu(context, moreButton)
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

    private fun showAddTabDialog() {
        val dialog = TabAddDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "AddTabDialog")
    }
}

class MyXAxisValueFormatter : ValueFormatter() {

    private val dateFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)

    override fun getFormattedValue(value: Float): String {
        // Assuming value represents incremental index
        val date = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, value.toInt()) // Adjust based on your data structure
        }.time
        return dateFormat.format(date)
    }
}

