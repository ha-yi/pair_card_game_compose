package com.hayi.vampair.domain.store

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity

class StoreData(private val context: ComponentActivity) {
    private val sharedPref: SharedPreferences = context.getPreferences(Context.MODE_PRIVATE)

    val SHOW_IMAGE_UPON_SUCCESS: String = "show_image_upon_success"

    fun setShowImageUponSuccess(show: Boolean) {
        with (sharedPref.edit()) {
            putBoolean(SHOW_IMAGE_UPON_SUCCESS, show)
            apply()
        }
    }

    fun getShowImageUponSuccess(): Boolean {
        return sharedPref.getBoolean(SHOW_IMAGE_UPON_SUCCESS, false)
    }
}
