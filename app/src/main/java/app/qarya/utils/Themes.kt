package app.qarya.utils

import android.content.Context
import app.qarya.R
import app.qarya.model.shared.YDUserManager

public class Themes{
    companion object {
        val DEFAULT = "DEFAULT"
        val DAY = "DAY"

        public fun saveTheme(value: String, context:Context) {
            val editor = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            //val editor = getPreferences(Activity.MODE_PRIVATE).edit()
            editor.putString("theme", value)
            editor.commit()
        }

        public fun getSavedTheme(context:Context): Int {
            val theme = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("theme", DEFAULT)
            when (theme) {
                DEFAULT -> return R.style.AppTheme_Transparent
                DAY -> return R.style.AppTheme_Day
                else -> return R.style.AppTheme_Transparent
            }
        }

        fun isDarkTheme(context:Context): Boolean {
            val theme = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("theme", DEFAULT)
            when (theme) {
                DEFAULT -> return true
                DAY -> return false
                else -> return true
            }
        }


        public fun toggleTheme(context:Context): Int {
            val theme = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("theme", DEFAULT)
            when (theme) {
                DEFAULT -> {
                    saveTheme(DAY, context)
                    return R.style.AppTheme_Transparent
                }
                DAY -> {
                    saveTheme(DEFAULT, context)
                    return R.style.AppTheme_Day
                }
                else -> {
                    saveTheme(DAY, context)
                    return R.style.AppTheme_Transparent
                }
            }
        }
    }
}