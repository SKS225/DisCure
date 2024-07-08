package com.example.discure.repository

import androidx.lifecycle.LiveData
import com.example.discure.database.OnDataBaseAction
import com.example.discure.models.Task

class ReminderRepository(private val reminderDao: OnDataBaseAction) {

    fun getAllTasks(): LiveData<List<Task>> = reminderDao.allTasksList


    suspend fun getTask(taskId: Int): Task? {
        return reminderDao.selectDataFromAnId(taskId)
    }

    suspend fun deleteTask(taskId: Int) {
        reminderDao.deleteTaskFromId(taskId)
    }

    suspend fun insertTask(task: Task) {
        reminderDao.insertDataIntoTaskList(task)
    }

    suspend fun updateTask(
        taskId: Int,
        taskTitle: String?,
        taskDescription: String?,
        taskDate: String?,
        taskTime: String?,
        taskEvent: String?
    ) {
        reminderDao.updateAnExistingRow(
            taskId,
            taskTitle,
            taskDescription,
            taskDate,
            taskTime,
            taskEvent
        )
    }
}
