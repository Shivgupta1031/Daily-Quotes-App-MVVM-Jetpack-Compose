package com.devshiv.dailyquotes

import android.app.Application
import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.utils.getAppVersion
import com.devshiv.dailyquotes.utils.getUserDeviceId
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        App.app_version = getAppVersion()
        App.device_id = getUserDeviceId()
    }

    companion object {
        var device_id = ""
        var app_version = ""
        var settings: SettingsEntity = SettingsEntity()
        var curUser = ""
    }
}