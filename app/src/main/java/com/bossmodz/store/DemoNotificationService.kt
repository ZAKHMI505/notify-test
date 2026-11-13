package com.bossmodz.store

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DemoNotificationService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var sequence = 0

    private val ticker = object : Runnable {
        override fun run() {
            sequence += 1
            showDemoNotification(sequence)
            handler.postDelayed(this, INTERVAL_MS)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CH_SERVICE, "Boss Modz Store service", NotificationManager.IMPORTANCE_LOW)
        )
        manager.createNotificationChannel(
            NotificationChannel(CH_DEMO, "Boss Modz Store demo updates", NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val foreground: Notification = NotificationCompat.Builder(this, CH_SERVICE)
            .setSmallIcon(R.drawable.ic_notify)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.boss_modz_logo))
            .setContentTitle("Boss Modz Store")
            .setContentText("DEMO notification test is running")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        startForeground(SERVICE_ID, foreground)
        handler.removeCallbacks(ticker)
        handler.post(ticker)
        return START_STICKY
    }

    private fun showDemoNotification(number: Int) {
        val orderNumber = 7800 + number
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val states = arrayOf(
            "Order Confirmed" to "DEMO • Sample order #BM-$orderNumber was received.",
            "Order Processing" to "DEMO • Sample order #BM-$orderNumber is being prepared.",
            "Order Ready" to "DEMO • Sample order #BM-$orderNumber is ready for review.",
            "Support Update" to "DEMO • You have a sample update from Boss Modz Store."
        )
        val (title, message) = states[(number - 1) % states.size]
        val logo = BitmapFactory.decodeResource(resources, R.drawable.boss_modz_logo)

        val notification = NotificationCompat.Builder(this, CH_DEMO)
            .setSmallIcon(R.drawable.ic_notify)
            .setLargeIcon(logo)
            .setContentTitle("Boss Modz Store • DEMO")
            .setContentText("$title — $message")
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title\n$message\nTime: $time"))
            .setColor(0xFF0756D8.toInt())
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(number, notification)
        } catch (_: SecurityException) {
            // User may revoke notification permission while the service is running.
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(ticker)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val CH_SERVICE = "boss_modz_service"
        private const val CH_DEMO = "boss_modz_demo_updates"
        private const val SERVICE_ID = 5050
        private const val INTERVAL_MS = 30_000L
    }
}
