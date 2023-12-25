package com.devshiv.dailyquotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devshiv.dailyquotes.db.dao.SettingsDao
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.db.entity.UsersEntity

@Database(entities = [SettingsEntity::class, UsersEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
    abstract fun usersDao(): UsersDao

}