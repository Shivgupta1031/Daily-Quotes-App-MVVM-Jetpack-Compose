package com.devshiv.dailyquotes.utils

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.devshiv.dailyquotes.utils.Constants.TAG
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


class Utils {

    companion object {
        fun randomColor(): Int {
            val red = Random.nextInt(0, 256)
            val green = Random.nextInt(0, 256)
            val blue = Random.nextInt(0, 256)

            return android.graphics.Color.rgb(red, green, blue)
        }

        fun getTodayDate(): String {
            val pattern = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val currentDate = Date()
            return dateFormat.format(currentDate)
        }

        fun isDateGreaterThanToday(dateString: String): Boolean {
            if (dateString.trim().isEmpty()) {
                return false
            }
            val pattern = "yyyy-MM-dd"

            // Parse the input date string
            val inputDate = parseDateString(dateString, pattern)

            // Get the current date
            val currentDate = Date()

            inputDate.hours = 0
            inputDate.minutes = 0
            inputDate.seconds = 0

            currentDate.hours = 0
            currentDate.minutes = 0
            currentDate.seconds = 0

            // Compare the input date with the current date
            return inputDate.date >= currentDate.date
        }

        private fun parseDateString(dateString: String, pattern: String): Date {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return dateFormat.parse(dateString) ?: Date()
        }

        fun updateMetadataInManifest(activity: Activity, key: String?, value: String?) {
            try {
                val ai = activity.packageManager.getApplicationInfo(
                    activity.packageName,
                    PackageManager.GET_META_DATA
                )
                val bundle = ai.metaData
                bundle.putString(key, value)
                // Reflectively update the metadata
                val metaData = Bundle()
                metaData.putBundle("metaData", bundle)
                val metaDataField: Field = ApplicationInfo::class.java.getDeclaredField("metaData")
                metaDataField.setAccessible(true)
                metaDataField.set(ai, metaData)
            } catch (e: Exception) {
                Log.d(TAG, "updateMetadataInManifest: " + e.message)
            }
        }
    }
}