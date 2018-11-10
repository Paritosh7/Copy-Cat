package com.example.paritosh.copycat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switch = findViewById<Switch>(R.id.permissionSwitch)
        switch.isChecked = PermissionHelper.loadBackgroundPermission(this)
        switch.setOnCheckedChangeListener { _, isChecked ->
            PermissionHelper.updateBackgroundPermission(this, isChecked)
            postActionToCatService(
                if (isChecked)
                    CopyService.ACTION_START_FOREGROUND_SERVICE
                else
                    CopyService.ACTION_STOP_FOREGROUND_SERVICE
            )
        }
    }
}


