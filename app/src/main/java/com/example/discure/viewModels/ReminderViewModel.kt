package com.example.discure.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discure.models.Reminder
import com.example.discure.models.Task
import com.example.discure.repository.MedReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderViewModel(private val repository: MedReminderRepository) : ViewModel() {

    val reminders: LiveData<List<Task>> = repository.getAllTasks()

    /*private val _task = MutableLiveData<Task?>()
    val task: MutableLiveData<Task?> get() = _task

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            _task.value = withContext(Dispatchers.IO) {
                dataBaseAction.selectDataFromAnId(taskId)
            }
        }
    }*/

    private val _reminder = MutableLiveData<Task?>()
    val reminder: MutableLiveData<Task?> get() = _reminder

    fun getTask(reminderId: Int) {
        viewModelScope.launch {
            _reminder.value = withContext(Dispatchers.IO) {
                repository.getTask(reminderId)
            }
        }
    }

    fun deleteTask(reminderId: Int) {
        viewModelScope.launch {
            repository.deleteTask(reminderId)
        }
    }

    fun insertTask(reminder: Reminder) {
        viewModelScope.launch {
            //repository.insertTask(reminder)
        }
    }

    fun updateReminder(
        taskId: Int,
        taskTitle: String?,
        taskDescription: String?,
        taskDate: String?,
        taskTime: String?,
        taskEvent: String?
    ) {
        viewModelScope.launch {
            repository.updateTask(
                taskId,
                taskTitle,
                taskDescription,
                taskDate,
                taskTime,
                taskEvent
            )
        }
    }
}
