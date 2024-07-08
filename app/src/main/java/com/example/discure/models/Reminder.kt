package com.example.discure.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "reminder")
class Reminder : Serializable {
    @PrimaryKey(autoGenerate = true)
    var reminderId: Int = 0

    @ColumnInfo(name = "reminderTitle")
    var reminderTitle: String? = null

    @ColumnInfo(name = "reminderIcon")
    var reminderIcon: Int? = null

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "medicines")
    var medicines: String? = null

    @ColumnInfo(name = "firstAlarmTime")
    var firstAlarmTime: String? = null

    @ColumnInfo(name = "secondAlarmTime")
    var secondAlarmTime: String? = null

    @ColumnInfo(name = "lastAlarm")
    var lastAlarm: String? = null
}