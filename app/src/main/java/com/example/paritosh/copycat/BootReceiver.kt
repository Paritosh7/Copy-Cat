package com.example.paritosh.copycat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val copyServiceIntent = Intent(context, CopyService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context?.startForegroundService(copyServiceIntent)
            else
                context?.startService(copyServiceIntent)
        }
    }
}