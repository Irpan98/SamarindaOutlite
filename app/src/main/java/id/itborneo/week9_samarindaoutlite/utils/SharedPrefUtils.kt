package id.itborneo.week9_samarindaoutlite.utils

import android.app.Activity
import android.content.Context

class SharedPrefUtils(private val activity: Activity) {
    companion object {
        const val SHAREDPREF_KEY_Bool = "savedBool"
    }

    fun putBooleanData(boolean: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(SHAREDPREF_KEY_Bool, boolean)
            commit()
        }
    }

    fun getBool(): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return false
        val defaultValue = false
        return sharedPref.getBoolean(SHAREDPREF_KEY_Bool, defaultValue)
    }

    fun removeBool() {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(SHAREDPREF_KEY_Bool)
            commit()
        }
    }
}

