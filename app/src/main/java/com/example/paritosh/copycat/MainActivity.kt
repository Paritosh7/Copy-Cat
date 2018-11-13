package com.example.paritosh.copycat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val OVERLAY_PERMISSION_REQUEST_CODE = 10101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (PermissionHelper.isOverlayPermissionAvailable(this)) {
                PermissionHelper.updateCatServiceStatus(this, isChecked)
                postActionToCatService(
                    if (isChecked)
                        CopyService.ACTION_START_FOREGROUND_SERVICE
                    else
                        CopyService.ACTION_STOP_FOREGROUND_SERVICE
                )
            } else {
                permissionSwitch.isChecked = false
                requestOverlayPermission()
            }
        }
        permissionSwitch.isChecked = PermissionHelper.isCatServiceEnabled(this)
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (PermissionHelper.isOverlayPermissionAvailable(this)) {
                permissionSwitch.isChecked = true
            } else {
                Toast.makeText(this, R.string.overlay_permission_unavailable_message, Toast.LENGTH_LONG).show()
            }
        }
    }
}


