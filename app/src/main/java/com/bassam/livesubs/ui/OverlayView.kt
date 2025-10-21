package com.bassam.livesubs.ui

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class OverlayView(ctx: Context) {
    private val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val tv = TextView(ctx).apply {
        textSize = 18f
        setTextColor(Color.WHITE)
        setBackgroundColor(0x88000000.toInt())
        gravity = Gravity.CENTER
        text = "جاهز للترجمة..."
        setPadding(24, 12, 24, 12)
    }

    private val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        if (android.os.Build.VERSION.SDK_INT >= 26)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    ).apply { gravity = Gravity.BOTTOM; y = 40 }

    fun attach() = wm.addView(tv, params)
    fun detach() = wm.removeView(tv)
    fun setText(t: String) { tv.text = t }
}
