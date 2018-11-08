package com.example.paritosh.copycat

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager

class CatOverlay constructor(context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val params =
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

    private val view = LayoutInflater.from(context).inflate(R.layout.cat_overlay_layout, null)

    var isShowing = false

    init {
        val displayRect = Rect()
        windowManager.defaultDisplay.getRectSize(displayRect)

        params.windowAnimations = R.style.CatButtonAnimations
        params.gravity = Gravity.END or Gravity.BOTTOM or Gravity.DISPLAY_CLIP_HORIZONTAL
        params.x = (context.resources.getDimension(R.dimen.cat_button_size) * -context.resources.getFraction(
            R.fraction.cat_button_overflow_x, 1, 1
        )).toInt()
        params.y = (displayRect.bottom * context.resources.getFraction(
            R.fraction.cat_button_position_y, 1, 1
        )).toInt()
    }

    fun show() {
        windowManager.addView(view, params)
        isShowing = true
    }

    fun dismiss() {
        windowManager.removeView(view)
        isShowing = false
    }

    fun setOnCatButtonClickListener(onCatButtonClickListener: OnCatButtonClickListener) {
        view.findViewById<CatButtonLayout>(R.id.catButton).setOnChildButtonClickListener { _, view ->
            when (view.id) {
                R.id.web -> onCatButtonClickListener.onCatButtonClick(OnCatButtonClickListener.ButtonType.WEB)
                R.id.dictionary -> onCatButtonClickListener.onCatButtonClick(OnCatButtonClickListener.ButtonType.DICTIONARY)
                R.id.translate -> onCatButtonClickListener.onCatButtonClick(OnCatButtonClickListener.ButtonType.TRANSLATE)
            }
        }
    }

    interface OnCatButtonClickListener {
        enum class ButtonType {
            WEB,
            DICTIONARY,
            TRANSLATE
        }
        fun onCatButtonClick(buttonType: ButtonType)
    }
}