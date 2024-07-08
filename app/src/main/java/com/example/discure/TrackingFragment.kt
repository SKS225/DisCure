package com.example.discure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.discure.adapters.TabPagerAdapter
import com.example.discure.databinding.FragmentTrackingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class TrackingFragment : Fragment(), TabAddDialogFragment.OnTabAddedListener {

    private lateinit var binding: FragmentTrackingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout


    private val tabTitles = mutableListOf("Weight", "Cholesterol", "Sugar Level", "+")
    private val tabFragments = mutableListOf(
        HealthTrackFragment.newInstance("Weight"),
        HealthTrackFragment.newInstance("Cholesterol"),
        HealthTrackFragment.newInstance("Sugar Level"),
        HealthTrackFragment.newInstance("Add New")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.tabPager
        tabLayout = binding.tabNav

        viewPager.adapter = TabPagerAdapter(requireActivity(), tabFragments)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()


        tabLayout.getTabAt(tabTitles.size - 1)?.view?.setOnClickListener {
            showAddTabDialog()
        }
    }

    private fun showAddTabDialog() {
        val dialog = TabAddDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "AddTabDialog")
    }

    override fun onTabAdded(type: String) {
        tabTitles.add(tabTitles.size - 1, type)
        tabFragments.add(tabTitles.size - 1, HealthTrackFragment.newInstance(type))
        viewPager.adapter?.notifyDataSetChanged()
        tabLayout.getTabAt(tabTitles.size - 2)?.select()
    }
}