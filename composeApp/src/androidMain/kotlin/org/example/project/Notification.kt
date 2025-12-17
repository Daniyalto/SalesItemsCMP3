package org.example.project

import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

actual class NotificationManager {
    private var context: Context? = null
    private val channelId = "sales_items_channel"

    fun initialize(context: Context) {
        this.context = context
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val ctx = context ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sales Items",
                AndroidNotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for sales items"
            }

            // Bruger Context til at hente system service
            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    actual fun showNotification(title: String, message: String) {
        val ctx = context ?: return
        val notificationManager = NotificationManagerCompat.from(ctx)

        val notification = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            println("Notification permission not granted: ${e.message}")
        }
    }

    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        val ctx = context ?: return
        val notificationManager = NotificationManagerCompat.from(ctx)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            onResult(notificationManager.areNotificationsEnabled())
        } else {
            onResult(true)
        }
    }
}
@Composable
actual fun rememberNotificationManager(): NotificationManager {
    val context = LocalContext.current.applicationContext

    return remember {
        NotificationManager().apply {
            initialize(context)
        }
    }
}
