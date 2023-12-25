package com.devshiv.dailyquotes.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_db")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 1,
    @ColumnInfo(name = "one_signal_app_id")
    var one_signal_app_id: String = "",
    @ColumnInfo(name = "privacy_policy")
    var privacy_policy: String = "",
    @ColumnInfo(name = "telegram_link")
    var telegram_link: String = "",
)
