package com.example.paritosh.copycat

import android.content.Context
import android.content.Intent
import android.os.Build

fun Context.postActionToCatService(action: String, fromBackground: Boolean = false) {
    val intent = Intent(this, CopyService::class.java)
    intent.action = action
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && fromBackground)
        this.startForegroundService(intent)
    else
        this.startService(intent)
}