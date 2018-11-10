package com.example.paritosh.copycat

import android.content.Context

object PermissionHelper {

    private const val BACKGROUND_PREFERENCES = "BACKGROUND_PREFERENCES"
    private const val BACKGROUND_PERMISSION_KEY = "BACKGROUND_PERMISSION_KEY"

    fun loadBackgroundPermission(context: Context) =
        context.getSharedPreferences(BACKGROUND_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(BACKGROUND_PERMISSION_KEY, false)

    fun updateBackgroundPermission(context: Context, enabled: Boolean) =
        context.getSharedPreferences(BACKGROUND_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(BACKGROUND_PERMISSION_KEY, enabled)
            .apply()
}