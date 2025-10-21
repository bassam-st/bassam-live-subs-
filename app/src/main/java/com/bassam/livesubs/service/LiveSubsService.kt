package com.bassam.livesubs.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bassam.livesubs.R
import com.bassam.livesubs.stt.SttManager
import com.bassam.livesubs.translate.Translator
import com.bassam.livesubs.ui.OverlayView
import kotlinx.coroutines.*

class LiveSubsService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var overlay: OverlayView
    private lateinit var stt: SttManager

    override fun onCreate() {
        super.onCreate()
        overlay = OverlayView(this)
        overlay.attach()
        startForeground(1, buildNotif())

        stt = SttManager(this) { text ->
            scope.launch {
                val translated = withContext(Dispatchers.IO) {
                    runCatching { Translator.toArabic(text) }.getOrDefault(text)
                }
                overlay.setText(translated)
            }
        }
        stt.start()
    }

    override fun onDestroy() {
        stt.stop()
        overlay.detach()
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun buildNotif(): Notification {
        val id = "live_subs_channel"
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel(id, "Live Subtitles", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
        return NotificationCompat.Builder(this, id)
            .setContentTitle("Bassam Live Subs")
            .setContentText("يعمل الآن ...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }
}
