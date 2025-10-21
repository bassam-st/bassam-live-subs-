package com.bassam.livesubs.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bassam.livesubs.R
import com.bassam.livesubs.service.LiveSubsService

class MainActivity : AppCompatActivity() {

    private val micPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startService(Intent(this, LiveSubsService::class.java))
        else Toast.makeText(this, "إذن الميكروفون مطلوب", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOverlay = findViewById<android.widget.Button>(R.id.btnOverlay)
        val btnStart = findViewById<android.widget.Button>(R.id.btnStart)
        val btnStop = findViewById<android.widget.Button>(R.id.btnStop)

        btnOverlay.setOnClickListener {
            val i = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(i)
        }

        btnStart.setOnClickListener { micPerm.launch(Manifest.permission.RECORD_AUDIO) }

        btnStop.setOnClickListener {
            stopService(Intent(this, LiveSubsService::class.java))
            Toast.makeText(this, "تم الإيقاف", Toast.LENGTH_SHORT).show()
        }
    }
}
