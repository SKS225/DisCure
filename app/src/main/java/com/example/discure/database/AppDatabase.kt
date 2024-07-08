package com.example.discure.database

import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import com.example.discure.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dataBaseAction(): OnDataBaseAction?

    /*protected fun createOpenHelperNonNull(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        return super.openHelper
    }

    protected fun createOpenHelperNullable(config: DatabaseConfiguration?): SupportSQLiteOpenHelper? {
        return super.openHelper
    }*/

    override fun createInvalidationTracker(): InvalidationTracker {
        return super.invalidationTracker
    }

    override fun clearAllTables() {
    }

    /*companion object {
        private var INSTANCE: AppDatabase? = null

        @Volatile
private var INSTANCE: AppDatabase? = null

fun getInstance(context: Context): AppDatabase {
    return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            App
    }
}*/
}