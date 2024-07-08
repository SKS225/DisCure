package com.example.discure

import android.app.AlarmManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.discure.databinding.FragmentAddMedBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MedAddFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentAddMedBinding
    private lateinit var alarmManager: AlarmManager
    private var isEdit: Boolean = false
    private var mHour: Int = 0
    private val mMin: Int = 0
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMedBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    private val mBottomSheetBehaviorCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    }
}
    /*fun setTaskId(
        taskId: Int,
        isEdit: Boolean,
        setRefreshListener: setRefreshListener,
        activity: MedReminderActivity
    ) {
        this.taskId = taskId
        this.isEdit = isEdit
        this.activity = activity
        this.setRefreshListener = setRefreshListener
    }
    override fun setupDialog(dialog: Dialog, style:Int){
        super.setupDialog(dialog, style)
        val contentView: View = View.inflate(context,R.layout.fragment_add_med,null)
        dialog.setContentView(contentView)

        alarmManager = (requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager).also {
                alarmManager = it
            }

        binding.addTask.setOnClickListener {
            if (validateFields())
                createTask()
        }

        if (isEdit)
            showTaskFromId()

        binding.taskTime.setOnTouchListener { v, event ->
            if (event.action === MotionEvent.ACTION_UP) {
                val c: Calendar = Calendar.getInstance()
                mHour = c.get(Calendar.HOUR_OF_DAY)
                mMinute = c.get(Calendar.MINUTE)

                // Launch Time Picker Dialog
                timePickerDialog = TimePickerDialog(
                    activity,
                    { view12: TimePicker?, hourOfDay: Int, minute: Int ->
                        taskTime.setText("$hourOfDay:$minute")
                        timePickerDialog.dismiss()
                    }, mHour, mMinute, false
                )
                timePickerDialog.show()
            }
            return true
        }

    }

    private fun validateFields(): Boolean {
        if (binding.addMed.text.toString() == "") {
            Toast.makeText(activity, "Please enter a medicine name", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.addDosage.text.toString()=="") {
            Toast.makeText(activity, "Please enter dosage", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.taskTime.text.toString() == "") {
            Toast.makeText(activity, "Please enter time", Toast.LENGTH_SHORT).show()
            return false
        }
        /*else if (binding.addInterval.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter an event", Toast.LENGTH_SHORT).show()
            return false
        } */
        else
            return true
    }

    private fun createTask() {
        class saveTaskInBackend() : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(Void...: Void? voids)
            {
                Task createTask = new Task();
                createTask.setTaskTitle(addTaskTitle.getText().toString());
                createTask.setTaskDescrption(addTaskDescription.getText().toString());
                createTask.setDate(taskDate.getText().toString());
                createTask.setLastAlarm(taskTime.getText().toString());
                createTask.setEvent(taskEvent.getText().toString());

                if (!isEdit)
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction()
                        .insertDataIntoTaskList(createTask);
                else
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction()
                        .updateAnExistingRow(
                            taskId, addTaskTitle.getText().toString(),
                            addTaskDescription.getText().toString(),
                            taskDate.getText().toString(),
                            taskTime.getText().toString(),
                            taskEvent.getText().toString()
                        );

                return null;
            }

            override fun doInBackground(vararg params: Void?): Void {
                TODO("Not yet implemented")
            }


        }
    }

    fun createAnAlarm() {
        try {
            /*val items1: Array<String> = taskDate.getText().toString().split("-")
            val dd = items1[0]
            val month = items1[1]
            val year = items1[2]*/

            val itemTime: List<String> = binding.taskTime.text.toString().split(":")
            val hour = itemTime[0]
            val min = itemTime[1]

            val curCal: GregorianCalendar = GregorianCalendar()
            curCal.setTimeInMillis(System.currentTimeMillis())

            val cal: GregorianCalendar = GregorianCalendar()
            cal.set(GregorianCalendar.HOUR_OF_DAY, hour.toInt())
            cal.set(GregorianCalendar.MINUTE, min.toInt())
            cal.set(GregorianCalendar.SECOND, 0)
            cal.set(GregorianCalendar.MILLISECOND, 0)
            //cal.set(GregorianCalendar.DATE, dd.toInt())

            val alarmIntent = Intent(activity, AlarmBroadcastReceiver::class.java)
            alarmIntent.putExtra("TITLE", binding.addMed.toString())
            alarmIntent.putExtra("DESC", binding.addDosage.text.toString())
            //alarmIntent.putExtra("DATE", taskDate.getText().toString())
            alarmIntent.putExtra("TIME", binding.taskTime.text.toString())

            val pendingIntent = PendingIntent.getBroadcast(activity, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent)
            count++

            val intent = PendingIntent.getBroadcast(activity, count, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis() - 600000,
                intent
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis() - 600000,
                intent
            )
            count++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showTaskFromId() {
        class ShowTaskFromId : AsyncTask<Void?, Void?, Void?>() {
            @Deprecated("Deprecated in Java")
            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Void?): Void? {
                task = DatabaseClient.getInstance(activity).getAppDatabase().dataBaseAction().selectDataFromAnId(taskId)
                return null
            }

            @Deprecated("Deprecated in Java")
            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                setDataInUI()
            }
        }

        val st = ShowTaskFromId()
        st.execute()
    }

    private fun setDataInUI() {
        binding.addMed.text = task
        addTaskDescription.setText(task.getTaskDescrption())
        taskDate.setText(task.getDate())
        taskTime.setText(task.getLastAlarm())
        taskEvent.setText(task.getEvent())
    }

    interface setRefreshListener {
        fun refresh()
    }
        
       */