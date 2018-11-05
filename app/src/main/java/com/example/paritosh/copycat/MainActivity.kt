package com.example.paritosh.copycat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch

class MainActivity : AppCompatActivity() {

    private val settingSharedPreferences = "settingSharedPreferences"
    private val permissionBoolean = "settingSharedPreferences"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switch = findViewById<Switch>(R.id.permissionSwitch)
        val sharedPreferences = getSharedPreferences(settingSharedPreferences, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()



        switch.isChecked = sharedPreferences.getBoolean(permissionBoolean, false)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                modifyingPreferences(editor, isChecked)
                settingIntent(CopyService.ACTION_START_FOREGROUND_SERVICE)
            } else {
                modifyingPreferences(editor, isChecked)
                settingIntent(CopyService.ACTION_STOP_FOREGROUND_SERVICE)
            }
        }
    }

    private fun settingIntent(setServiceState: String) {
        val intent = Intent(this@MainActivity, CopyService::class.java)
        intent.action = setServiceState
        startService(intent)
    }

    private fun modifyingPreferences(editor: SharedPreferences.Editor, isChecked: Boolean) {
        editor.putBoolean(permissionBoolean, isChecked)
        editor.apply()
    }
}


