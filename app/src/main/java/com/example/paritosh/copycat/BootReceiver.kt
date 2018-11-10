package com.example.paritosh.copycat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (PermissionHelper.loadBackgroundPermission(context))
                context.postActionToCatService(CopyService.ACTION_START_FOREGROUND_SERVICE, true)
        }
    }
}