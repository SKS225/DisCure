package com.example.discure

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discure.adapters.RemindersAdapter
import com.example.discure.broadcastReceiver.AlarmBroadcastReceiver
import com.example.discure.database.DatabaseClient
import com.example.discure.databinding.ActivityTrackMedBinding
import com.example.discure.repository.MedReminderRepository
import com.example.discure.viewModelFactory.MedReminderViewModelFactory
import com.example.discure.viewModels.MedReminderViewModel


class MedReminderActivity : AppCompatActivity(), AddReminderFragment.RefreshListener {
    private lateinit var binding: ActivityTrackMedBinding
    private lateinit var remindersAdapter: RemindersAdapter

    private val medReminderRepository: MedReminderRepository by lazy {
        val db = DatabaseClient.getInstance(applicationContext)?.appDatabase
        MedReminderRepository(db!!.dataBaseAction()!!)
    }

    private val medReminderViewModel: MedReminderViewModel by viewModels {
        MedReminderViewModelFactory(medReminderRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
        setupObservers()

        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //Glide.with(applicationContext).load(R.drawable.first_note).into<Target<Drawable>>(noDataImage)

        val receiver = ComponentName(this, AlarmBroadcastReceiver::class.java)
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        binding.btnAddReminder.setOnClickListener {
            val addReminderFragment = AddReminderFragment()
            addReminderFragment.setTaskId(0, false, this, this@MedReminderActivity)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addReminderFragment)
                .addToBackStack(null) // Add to back stack so the user can navigate back
                .commit()
        }
    }

    private fun setUpAdapter() {
        remindersAdapter = RemindersAdapter(this@MedReminderActivity, emptyList(), medReminderViewModel,supportFragmentManager,this)
        binding.rvReminders.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvReminders.adapter = remindersAdapter
    }

    private fun setupObservers() {
        medReminderViewModel.tasks.observe(this) { tasks ->
            remindersAdapter.updateTasks(tasks)
            binding.noDataImage.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun refresh() {
        //getSavedTasks()
    }
}