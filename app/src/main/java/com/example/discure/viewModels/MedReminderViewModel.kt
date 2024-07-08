package com.example.discure.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discure.models.Task
import com.example.discure.repository.MedReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedReminderViewModel(private val repository: MedReminderRepository) : ViewModel() {

    val tasks: LiveData<List<Task>> = repository.getAllTasks()

    /*private val _task = MutableLiveData<Task?>()
    val task: MutableLiveData<Task?> get() = _task

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            _task.value = withContext(Dispatchers.IO) {
                dataBaseAction.selectDataFromAnId(taskId)
            }
        }
    }*/

    private val _task = MutableLiveData<Task?>()
    val task: MutableLiveData<Task?> get() = _task

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            _task.value = withContext(Dispatchers.IO) {
                repository.getTask(taskId)
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(
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
