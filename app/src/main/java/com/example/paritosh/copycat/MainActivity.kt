package com.example.paritosh.copycat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch

class MainActivity : AppCompatActivity() {

    companion object {
        const val preferences = "preferences"
        const val permissionBoolean = "permissionBoolean"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switch = findViewById<Switch>(R.id.permissionSwitch)

        val sharedPreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE)

        switch.isChecked = sharedPreferences.getBoolean(permissionBoolean, false)
        switch.setOnCheckedChangeListener { _, isChecked ->
            modifyingPreferences(sharedPreferences, isChecked)
            postActionToCatService(
                if (isChecked)
                    CopyService.ACTION_START_FOREGROUND_SERVICE
                else
                    CopyService.ACTION_STOP_FOREGROUND_SERVICE
            )
        }
    }

    private fun postActionToCatService(action: String) {
        val intent = Intent(this@MainActivity, CopyService::class.java)
        intent.action = action
        startService(intent)
    }

    private fun modifyingPreferences(sharedPreferences: SharedPreferences, isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(permissionBoolean, isChecked)
        editor.apply()
    }
}


