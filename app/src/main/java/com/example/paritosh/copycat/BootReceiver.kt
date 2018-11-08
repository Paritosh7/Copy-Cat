package com.example.paritosh.copycat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.d("BroadCast", "outside if")
        val sharedPreferences = context?.getSharedPreferences(MainActivity.preferences, Context.MODE_PRIVATE)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (sharedPreferences?.getBoolean(MainActivity.permissionBoolean, false)!!)
                postActionToCatService(CopyService.ACTION_START_FOREGROUND_SERVICE, context)
        }
    }

    private fun postActionToCatService(action: String, context: Context?) {
        val intent = Intent(context, CopyService::class.java)
        intent.action = action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context?.startForegroundService(intent)
        else
            context?.startService(intent)
    }
}