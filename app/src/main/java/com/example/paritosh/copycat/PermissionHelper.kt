package com.example.paritosh.copycat

import android.content.Context
import android.os.Build
import android.provider.Settings

object PermissionHelper {

    private const val CAT_PERMISSION_PREFERENCES = "CAT_PERMISSION_PREFERENCES"
    private const val CAT_SERVICE_STATUS_KEY = "CAT_SERVICE_STATUS_KEY"

    fun isCatServiceEnabled(context: Context) =
        context.getSharedPreferences(CAT_PERMISSION_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(CAT_SERVICE_STATUS_KEY, false)

    fun updateCatServiceStatus(context: Context, enabled: Boolean) =
        context.getSharedPreferences(CAT_PERMISSION_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(CAT_SERVICE_STATUS_KEY, enabled)
            .apply()

    fun isOverlayPermissionAvailable(context: Context): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(context)
    } else {
        true
    }
}