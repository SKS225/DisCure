package com.example.discure.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.discure.models.Task

@Dao
interface OnDataBaseAction {
    @get:Query("SELECT * FROM Task")
    val allTasksList: LiveData<List<Task>>

    @Query("DELETE FROM Task")
    suspend fun truncateTheList()

    @Insert
    suspend fun insertDataIntoTaskList(task: Task)

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    suspend fun deleteTaskFromId(taskId: Int)

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    suspend fun selectDataFromAnId(taskId: Int): Task?

    @Query(
        "UPDATE Task SET taskTitle = :taskTitle, taskDescription = :taskDescription, date = :taskDate, " +
                "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId"
    )
    suspend fun updateAnExistingRow(
        taskId: Int,
        taskTitle: String?,
        taskDescription: String?,
        taskDate: String?,
        taskTime: String?,
        taskEvent: String?
    )
}