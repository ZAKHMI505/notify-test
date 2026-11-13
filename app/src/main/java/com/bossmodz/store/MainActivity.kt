package com.bossmodz.store

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private lateinit var status: TextView

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startDemo()
            else status.text = "Notifications are not allowed. Enable them in Android Settings."
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(36, 48, 36, 48)
            setBackgroundColor(0xFFF1F5FC.toInt())
        }

        val heading = TextView(this).apply {
            text = "BOSS MODZ STORE"
            textSize = 27f
            setTextColor(0xFF0756D8.toInt())
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "DEMO NOTIFICATION TEST"
            textSize = 14f
            setTextColor(0xFF53627A.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 28)
        }
        status = TextView(this).apply {
            text = "Stopped"
            textSize = 17f
            setTextColor(0xFF17243A.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 28)
        }

        val startButton = Button(this).apply {
            text = "Start demo notifications"
            setOnClickListener { requestPermissionAndStart() }
        }
        val stopButton = Button(this).apply {
            text = "Stop notifications"
            setOnClickListener {
                stopService(Intent(this@MainActivity, DemoNotificationService::class.java))
                status.text = "Stopped"
            }
        }
        val note = TextView(this).apply {
            text = "Sample order updates only. No real orders or payments are processed.\nA demo alert appears now and every 30 seconds while running."
            textSize = 13f
            setTextColor(0xFF65738A.toInt())
            gravity = Gravity.CENTER
            setPadding(8, 28, 8, 0)
        }

        root.addView(heading)
        root.addView(subtitle)
        root.addView(status)
        root.addView(startButton)
        root.addView(stopButton)
        root.addView(note)
        setContentView(root)
    }

    private fun requestPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            startDemo()
        }
    }

    private fun startDemo() {
        val intent = Intent(this, DemoNotificationService::class.java)
        ContextCompat.startForegroundService(this, intent)
        status.text = "Running — demo alert every 30 seconds"
    }
}
