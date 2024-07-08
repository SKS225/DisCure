package com.example.discure

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.discure.broadcastReceiver.AlarmBroadcastReceiver
import com.example.discure.database.DatabaseClient
import com.example.discure.databinding.FragmentCreateTaskBinding
import com.example.discure.models.Task
import com.example.discure.repository.MedReminderRepository
import com.example.discure.viewModelFactory.MedReminderViewModelFactory
import com.example.discure.viewModels.MedReminderViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.GregorianCalendar

class AddReminderFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding

    private var taskId: Int = 0
    private var isEdit: Boolean = false
    //var task: Task? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var setRefreshListener: RefreshListener
    private lateinit var alarmManager: AlarmManager
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    lateinit var activity: MedReminderActivity

    private val medReminderRepository: MedReminderRepository by lazy {
        val db = DatabaseClient.getInstance(requireContext())?.appDatabase
        MedReminderRepository(db!!.dataBaseAction()!!)
    }

    private val medReminderViewModel: MedReminderViewModel by viewModels {
        MedReminderViewModelFactory(medReminderRepository)
    }


    /*private val mBottomSheetBehaviorCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    }*/

    fun setTaskId(
        taskId: Int,
        isEdit: Boolean,
        setRefreshListener: MedReminderActivity,
        activity: MedReminderActivity
    ) {
        this.taskId = taskId
        this.isEdit = isEdit
        this.activity = activity
        this.setRefreshListener = setRefreshListener
    }
    //comment

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //super.setupDialog(dialog, style)
        binding = FragmentCreateTaskBinding.inflate(layoutInflater,container,false)
        //val contentView = View.inflate(context, R.layout.fragment_create_task, null)
        //unbinder = ButterKnife.bind(this, contentView)
        //dialog.setContentView(contentView)
        //dialog.setContentView(binding.root)

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.addTask.setOnClickListener {
            if (validateFields()) createTask()
        }
        if (isEdit) {
            medReminderViewModel.getTask(taskId)
            medReminderViewModel.task.observe(viewLifecycleOwner) { task ->
                task?.let {
                    setDataInUI(it)
                }
            }
        }

        binding.taskDate.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val c = Calendar.getInstance()
                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                datePickerDialog = DatePickerDialog(
                    requireActivity(),
                    { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        binding.taskDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        datePickerDialog.dismiss()
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            true
        }

        binding.taskTime.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                // Get Current Time
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]

                // Launch Time Picker Dialog
                timePickerDialog = TimePickerDialog(
                    getActivity(),
                    { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        binding.taskTime.setText("$hourOfDay:$minute")
                        timePickerDialog.dismiss()
                    }, mHour, mMinute, false
                )
                timePickerDialog.show()
            }
            true
        }
        return binding.root
    }

    private fun validateFields(): Boolean {
        if (binding.addTaskTitle.text.toString().equals("", ignoreCase = true)) {
            Toast.makeText(activity, "Please enter a valid title", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.addTaskDescription.text.toString().equals("", ignoreCase = true)) {
            Toast.makeText(activity, "Please enter a valid description", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.taskDate.text.toString().equals("", ignoreCase = true)) {
            Toast.makeText(activity, "Please enter date", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.taskTime.text.toString().equals("", ignoreCase = true)) {
            Toast.makeText(activity, "Please enter time", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.taskEvent.text.toString().equals("", ignoreCase = true)) {
            Toast.makeText(activity, "Please enter an event", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun createTask() {
        /*lifecycleScope.launch {
            val createTask = Task().apply {
                taskTitle = binding.addTaskTitle.text.toString()
                taskDescription = binding.addTaskDescription.text.toString()
                date = binding.taskDate.text.toString()
                lastAlarm = binding.taskTime.text.toString()
                event = binding.taskEvent.text.toString()
            }

            withContext(Dispatchers.IO) {
                if (!isEdit) {
                    DatabaseClient.getInstance(requireActivity())!!.appDatabase
                        .dataBaseAction()
                        ?.insertDataIntoTaskList(createTask)
                } else {
                    DatabaseClient.getInstance(requireActivity())!!.appDatabase
                        .dataBaseAction()
                        ?.updateAnExistingRow(
                            taskId,
                            binding.addTaskTitle.text.toString(),
                            binding.addTaskDescription.text.toString(),
                            binding.taskDate.text.toString(),
                            binding.taskTime.text.toString(),
                            binding.taskEvent.text.toString()
                        )
                }
            }

            createAnAlarm()
            setRefreshListener.refresh()
            Toast.makeText(requireActivity(), "Your event has been added", Toast.LENGTH_SHORT).show()
            //dismiss()
        }*/
        lifecycleScope.launch {
            val taskTitle = binding.addTaskTitle.text.toString()
            val taskDescription = binding.addTaskDescription.text.toString()
            val taskDate = binding.taskDate.text.toString()
            val taskTime = binding.taskTime.text.toString()
            val taskEvent = binding.taskEvent.text.toString()

            if (!isEdit) {
                val newTask = Task().apply {
                    this.taskTitle = taskTitle
                    this.taskDescription = taskDescription
                    this.date = taskDate
                    this.lastAlarm = taskTime
                    this.event = taskEvent
                }
                medReminderViewModel.insertTask(newTask)
            } else {
                medReminderViewModel.updateTask(
                    taskId,
                    taskTitle,
                    taskDescription,
                    taskDate,
                    taskTime,
                    taskEvent
                )
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

    /*private fun showTaskFromId() {
        lifecycleScope.launch {
            task = withContext(Dispatchers.IO) {
                DatabaseClient.getInstance(requireActivity())
                    ?.appDatabase
                    ?.dataBaseAction()
                    ?.selectDataFromAnId(taskId)
            }
            setDataInUI()
        }
    }*/


    private fun setDataInUI(task: Task) {
        binding.addTaskTitle.setText(task.taskTitle)
        binding.addTaskDescription.setText(task.taskDescription)
        binding.taskDate.setText(task.date)
        binding.taskTime.setText(task.lastAlarm)
        binding.taskEvent.setText(task.event)
    }

    interface RefreshListener {
        fun refresh()
    }

    companion object {
        var count: Int = 0
    }
}