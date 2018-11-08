package com.example.paritosh.copycat

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import java.util.*

class CopyService : Service() {

    companion object {
        private const val CHANNEL_ID: String = "channel_Id"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    }

    private var overlay: CatOverlay? = null

    override fun onCreate() {
        super.onCreate()
        registerClipboardListener()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_FOREGROUND_SERVICE -> startCatService()
            ACTION_STOP_FOREGROUND_SERVICE -> stopCatService()
        }
        return START_STICKY
    }

    private fun stopCatService() {
        stopForeground(true)
        stopSelf()
    }

    private fun startCatService() {
        createNotificationChannel()
        startForeground(11, loadNotification())
    }

    override fun onBind(intent: Intent?) = null

    private fun registerClipboardListener() {
        val clipBoard: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoard?.addPrimaryClipChangedListener {

            if (overlay == null || !overlay!!.isShowing)
                overlay = CatOverlay(this)

            overlay?.setOnCatButtonClickListener(object : CatOverlay.OnCatButtonClickListener {
                override fun onCatButtonClick(buttonType: CatOverlay.OnCatButtonClickListener.ButtonType) {
                    Toast.makeText(this@CopyService, buttonType.name, Toast.LENGTH_SHORT).show()
                    when (buttonType) {
                        CatOverlay.OnCatButtonClickListener.ButtonType.WEB -> {
                            webSearchIntent(clipBoard, buttonType)
                        }
                        CatOverlay.OnCatButtonClickListener.ButtonType.DICTIONARY -> {
                            webSearchIntent(clipBoard, buttonType)

                        }
                        CatOverlay.OnCatButtonClickListener.ButtonType.TRANSLATE -> {
                            webSearchIntent(clipBoard, buttonType)
                        }
                    }
                }
            })

            if (!overlay!!.isShowing) {
                overlay?.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        closeOverlay()
                    }
                }, resources.getInteger(R.integer.cat_button_timeout).toLong())
            }
        }
    }

    private fun webSearchIntent(
        clipBoard: ClipboardManager, buttonType: CatOverlay.OnCatButtonClickListener.ButtonType
    ) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)

        when (buttonType) {
            CatOverlay.OnCatButtonClickListener.ButtonType.WEB -> intent.putExtra(
                SearchManager.QUERY,
                clipBoard.primaryClip?.getItemAt(0)?.text
            )
            CatOverlay.OnCatButtonClickListener.ButtonType.DICTIONARY -> intent.putExtra(
                SearchManager.QUERY,
                "meaning: " + clipBoard.primaryClip?.getItemAt(0)?.text
            )
            else -> intent.putExtra(SearchManager.QUERY, "Translate " + clipBoard.primaryClip?.getItemAt(0)?.text)
        }
        startActivity(intent)
        closeOverlay()
    }

    private fun closeOverlay() {
        overlay?.dismiss()
        overlay = null
    }

    private fun loadNotification(): Notification {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Hello")
            .setContentText("remind")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


