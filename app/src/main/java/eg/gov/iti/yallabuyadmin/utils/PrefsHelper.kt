package eg.gov.iti.yallabuyadmin.utils

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("yallabuy_prefs", Context.MODE_PRIVATE)
    }

    fun setIsLoggedIn(value: Boolean) {
        prefs.edit().putBoolean("is_logged_in", value).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }
}
