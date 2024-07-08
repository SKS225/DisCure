package com.example.discure.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discure.AddReminderFragment
import com.example.discure.MedReminderActivity
import com.example.discure.R
import com.example.discure.models.Task
import com.example.discure.viewModels.MedReminderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemindersAdapter(
    private val context: MedReminderActivity,
    private var taskList: List<Task> = mutableListOf(),
    private val medReminderViewModel: MedReminderViewModel,
    private val fragmentManager: FragmentManager,
    private var setRefreshListener: AddReminderFragment.RefreshListener
) :
    RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    private var inputDateFormat: SimpleDateFormat = SimpleDateFormat("dd-M-yyyy", Locale.US)
    var date: Date? = null
    private var outputDateString: String? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = inflater.inflate(R.layout.item_task, viewGroup, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.taskTitle
        holder.description.text = task.taskDescription
        holder.time.text = task.lastAlarm
        holder.status.text = if (task.isComplete) "COMPLETED" else "UPCOMING"
        holder.options.setOnClickListener {
            showPopUpMenu(it, position)
        }

        try {
            date = inputDateFormat.parse(task.date!!)
            outputDateString = dateFormat.format(date!!)

            val items1 = outputDateString!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val day = items1[0]
            val dd = items1[1]
            val month = items1[2]

            holder.day.text = day
            holder.date.text = dd
            holder.month.text = month
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showPopUpMenu(view: View, position: Int) {
        val task = taskList[position]
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menuDelete -> {
                    deleteTaskFromId(task.taskId, position)
                }

                R.id.menuUpdate -> {
                    val addReminderFragment = AddReminderFragment()
                    addReminderFragment.setTaskId(task.taskId, true, context, context)
                    //addReminderFragment.show(context.supportFragmentManager, addReminderFragment.tag)
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addReminderFragment)
                        .addToBackStack(null) // Add to back stack so the user can navigate back
                        .commit()
                }

                R.id.menuComplete -> {
                    showCompleteDialog(task.taskId, position)
                }
            }
            false
        }
        popupMenu.show()
    }

    fun updateTasks(tasks: List<Task>) {
        this.taskList = tasks
        notifyDataSetChanged()
    }

    private fun showCompleteDialog(taskId: Int, position: Int) {
        val dialog = Dialog(context, R.style.AppTheme)
        dialog.setContentView(R.layout.dialog_completed_theme)
        val close = dialog.findViewById<Button>(R.id.closeButton)
        close.setOnClickListener {
            deleteTaskFromId(taskId, position)
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    private fun deleteTaskFromId(taskId: Int, position: Int) {
        medReminderViewModel.deleteTask(taskId)
        removeAtPosition(position)
        setRefreshListener.refresh()
    }

    private fun removeAtPosition(position: Int) {
        (taskList as MutableList).removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, taskList.size)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.findViewById(R.id.day)
        val date: TextView = itemView.findViewById(R.id.date)
        val month: TextView = itemView.findViewById(R.id.month)
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val status: TextView = itemView.findViewById(R.id.status)
        val options: ImageView = itemView.findViewById(R.id.options)
        val time: TextView = itemView.findViewById(R.id.time)
    }
}