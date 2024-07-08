package com.example.discure

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.discure.broadcastReceiver.AlarmBroadcastReceiver
import com.example.discure.database.DatabaseClient
import com.example.discure.databinding.FragmentNewReminderBinding
import com.example.discure.models.Reminder
import com.example.discure.models.Task
import com.example.discure.repository.MedReminderRepository
import com.example.discure.viewModelFactory.MedReminderViewModelFactory
import com.example.discure.viewModels.MedReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.GregorianCalendar


/*class NewReminderFragment : Fragment() {
    private lateinit var binding: FragmentNewReminderBinding
    private var reminderId: Int = 0
    private var isEdit: Boolean = false
    //lateinit var reminder: Reminder
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var amPm: Int = 0
    private var reminderTime: String = ""
    private lateinit var setRefreshListener: AddReminderFragment.RefreshListener
    private lateinit var alarmManager: AlarmManager
    lateinit var activity: MedReminderActivity

    private val medReminderRepository: MedReminderRepository by lazy {
        val db = DatabaseClient.getInstance(requireContext())?.appDatabase
        MedReminderRepository(db!!.dataBaseAction()!!)
    }

    private val medReminderViewModel: MedReminderViewModel by viewModels {
        MedReminderViewModelFactory(medReminderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.btnAddReminder.setOnClickListener {
            if (validateFields()) createReminder()
        }

        if (isEdit) {
            showReminderFromId()
        }

        binding.btnBack.setOnClickListener {
            onDestroy()
        }

        binding.npHour.minValue = 1
        binding.npHour.maxValue = 12
        binding.npHour.wrapSelectorWheel = true
        binding.npHour.value = 10
        binding.npHour.setOnValueChangedListener { _, _, newVal ->
            mHour = newVal
        }

        binding.npMinute.minValue = 0
        binding.npMinute.maxValue = 60
        binding.npMinute.setDisplayedValues(arrayOf("00","01","02","03","04","05","06","07","08","09"))
        binding.npMinute.wrapSelectorWheel = true
        binding.npMinute.setOnValueChangedListener { _, _, newVal ->
            mMinute = newVal
        }

        binding.npAmPm.minValue = 0
        binding.npAmPm.maxValue = 1
        binding.npAmPm.wrapSelectorWheel = false
        binding.npAmPm.setDisplayedValues(arrayOf("am", "pm"))
        binding.npAmPm.setOnValueChangedListener { _, _, newVal ->
            amPm = newVal
        }

        return binding.root
    }

    fun setReminderId(
        reminderId: Int,
        isEdit: Boolean,
        setRefreshListener: MedReminderActivity,
        activity: MedReminderActivity
    ) {
        this.reminderId = reminderId
        this.isEdit = isEdit
        this.activity = activity
        this.setRefreshListener = setRefreshListener
    }

    private fun validateFields(): Boolean {
        /*if (medicineList.isEmpty()) {
            Toast.makeText(activity, "Please add medicines", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }*/
        return true
    }

    private fun createReminder() {
        lifecycleScope.launch {
            val time = getReminderTime()
            val title = getTitle()
            val icon = getIcon()

            if (!isEdit) {
                val newReminder = Reminder().apply {
                    this.reminderTitle = title
                    this.reminderIcon = icon
                    //this.date = taskDate
                    this.lastAlarm = time
                }
                medReminderViewModel.insertTask(newReminder)
            } else {
                medReminderViewModel.updateTask(reminderId, time)
            }

            createAnAlarm()
            setRefreshListener.refresh()
            Toast.makeText(requireActivity(), "Your event has been added", Toast.LENGTH_SHORT).show()
            //dismiss()
        }
    }

    private fun createAnAlarm() {
        try {
            val items1 =
                binding.taskDate.text.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val dd = items1[0]
            //val month = items1[1]
            //val year = items1[2]

            val itemTime =
                binding.taskTime.text.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val hour = itemTime[0]
            val min = itemTime[1]

            val curCal: Calendar = GregorianCalendar()
            curCal.timeInMillis = System.currentTimeMillis()

            val cal: Calendar = GregorianCalendar()
            cal[Calendar.HOUR_OF_DAY] = hour.toInt()
            cal[Calendar.MINUTE] = min.toInt()
            cal[Calendar.SECOND] = 0
            cal[Calendar.MILLISECOND] = 0
            cal[Calendar.DATE] = dd.toInt()

            val alarmIntent = Intent(
                activity,
                AlarmBroadcastReceiver::class.java
            )
            alarmIntent.putExtra("TITLE", binding.addTaskTitle.text.toString())
            alarmIntent.putExtra("DESC", binding.addTaskDescription.text.toString())
            alarmIntent.putExtra("DATE", binding.taskDate.text.toString())
            alarmIntent.putExtra("TIME", binding.taskTime.text.toString())
            val pendingIntent = PendingIntent.getBroadcast(
                activity,
                count,
                alarmIntent,
                //PendingIntent.FLAG_UPDATE_CURRENT
                PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,
                pendingIntent
            )
            try {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
                Toast.makeText(activity, "Exact alarm permission is not granted", Toast.LENGTH_SHORT).show()
            }
            count++

            val intent = PendingIntent.getBroadcast(activity, count, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis - 600000,
                intent
            )
            val reminderIntent = PendingIntent.getBroadcast(
                activity, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, cal.timeInMillis - 600000, reminderIntent
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
                Toast.makeText(activity, "Exact alarm permission is not granted", Toast.LENGTH_SHORT).show()
            }
            count++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showReminderFromId() {
        lifecycleScope.launch {
            task = withContext(Dispatchers.IO) {
                DatabaseClient.getInstance(requireActivity())
                    ?.appDatabase
                    ?.dataBaseAction()
                    ?.selectDataFromAnId(taskId)
            }
            setDataInUI()
        }
    }


    private fun setDataInUI() {
        binding.addTaskTitle.setText(task!!.taskTitle)
        binding.addTaskDescription.setText(task!!.taskDescription)
        binding.taskDate.setText(task!!.date)
        binding.taskTime.setText(task!!.lastAlarm)
        binding.taskEvent.setText(task!!.event)
    }

    private fun getReminderTime(): String {
        val hour = if (amPm==1) "${mHour + 12}" else mHour
        return "$hour:$mMinute"
    }

    private fun getTitle(): String{
        val hour = if (amPm==1) mHour + 12 else mHour
        return if (hour<12)
            "Morning Pills"
        else if (hour in 12..16)
            "Afternoon Pills"
        else if (hour in 17..20)
            "Evening Pills"
        else
            "Night Pills"
    }

    private fun getIcon(): Int{
        val hour = if (amPm==1) mHour + 12 else mHour
        return if (hour<12)
            R.drawable.baseline_wb_sunny_24
        else if (hour in 12..16)
            R.drawable.baseline_wb_sunny_24
        else if (hour in 17..20)
            R.drawable.baseline_nightlight_round_24
        else
           R.drawable.baseline_nightlight_round_24
    }

    interface RefreshListener {
        fun refresh()
    }

    companion object {
        var count: Int = 0
    }

}*/