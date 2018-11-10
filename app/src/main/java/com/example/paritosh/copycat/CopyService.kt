package com.example.paritosh.copycat

import android.app.*
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule


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

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_FOREGROUND_SERVICE -> startCatService()
            ACTION_STOP_FOREGROUND_SERVICE -> stopCatService()
        }
        return START_STICKY
    }

    private fun startCatService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        startForeground(11, loadNotification())
    }

    private fun stopCatService() {
        stopForeground(true)
        stopSelf()
    }

    private fun registerClipboardListener() {
        val clipBoardManager: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardManager?.addPrimaryClipChangedListener {
            val text = clipBoardManager.primaryClip?.getItemAt(0)?.text

            if (text.isNullOrEmpty() || !text!!.isValidForSearch())
                return@addPrimaryClipChangedListener

            if (overlay == null || !overlay!!.isShowing)
                overlay = CatOverlay(this)

            overlay?.setOnCatButtonClickListener(object : CatOverlay.OnCatButtonClickListener {
                override fun onCatButtonClick(buttonType: CatOverlay.OnCatButtonClickListener.ButtonType) {
                    executeActionForText(text.toString(), buttonType)
                    closeOverlay()
                }
            })

            showOverlayWithTimeout()
        }
    }

    private fun showOverlayWithTimeout() {
        if (!overlay!!.isShowing) {
            overlay?.show()
            Timer().schedule(resources.getInteger(R.integer.cat_button_timeout).toLong()) {
                closeOverlay()
            }
        }
    }

    private fun closeOverlay() {
        overlay?.dismiss()
        overlay = null
    }

    private fun executeActionForText(text: String, buttonType: CatOverlay.OnCatButtonClickListener.ButtonType) {
        val intent = when (buttonType) {
            CatOverlay.OnCatButtonClickListener.ButtonType.WEB -> buildWebSearchIntent(text)
            CatOverlay.OnCatButtonClickListener.ButtonType.DICTIONARY -> buildDictionaryIntent(text)
            CatOverlay.OnCatButtonClickListener.ButtonType.TRANSLATE -> buildTranslateIntent(text)
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.generic_error_message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildWebSearchIntent(text: String) = Intent(Intent.ACTION_WEB_SEARCH).apply {
        putExtra(SearchManager.QUERY, text)
    }

    private fun buildDictionaryIntent(text: String) = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://www.dictionary.com/browse/$text")
    }

    private fun buildTranslateIntent(text: String) = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra("key_text_input", text)
        putExtra("key_text_output", "")
        putExtra("key_language_from", "en")
        putExtra("key_language_to", "de")
        putExtra("key_suggest_translation", "")
        putExtra("key_from_floating_window", false)
        component = ComponentName(
            "com.google.android.apps.translate",
            "com.google.android.apps.translate.TranslateActivity"
        )
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
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

    private fun CharSequence.isValidForSearch(): Boolean {
        return true
    }
}


