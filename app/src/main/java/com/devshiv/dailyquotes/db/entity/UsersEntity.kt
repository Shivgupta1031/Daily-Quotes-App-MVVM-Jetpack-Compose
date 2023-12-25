package com.devshiv.dailyquotes.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_db")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 1,
    @ColumnInfo(name = "username")
    var username: String = "",
    @ColumnInfo(name = "password")
    var password: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "phone_number")
    var phone_number: String = "",
    @ColumnInfo(name = "points")
    var points: String = "",
    @ColumnInfo(name = "device_id")
    var device_id: String = "",
    @ColumnInfo(name = "app_version")
    var app_version: String = "",
    @ColumnInfo(name = "last_active")
    var last_active: String = "",
    @ColumnInfo(name = "last_bonus")
    var last_bonus: String = "",
    @ColumnInfo(name = "today_task")
    var today_task: String = "",
    @ColumnInfo(name = "created")
    var created: String = "",
)
