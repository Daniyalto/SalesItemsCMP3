package org.example.project

import platform.UserNotifications.*
import platform.Foundation.NSError
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi

actual class NotificationManager {
    private val center = UNUserNotificationCenter.currentNotificationCenter()
    @OptIn(ExperimentalForeignApi::class)
    actual fun showNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(UNNotificationSound.defaultSound())
        }

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "sales_item_${(0..999999).random()}",
            content = content,
            trigger = null
        )

        center.addNotificationRequest(request) { error: NSError? ->
            if (error != null) {
                println("Error showing notification: ${error.localizedDescription}")
            }
        }
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or
                    UNAuthorizationOptionSound or
                    UNAuthorizationOptionBadge
        ) { granted, error ->
            onResult(granted)
        }
    }
}
@Composable
actual fun rememberNotificationManager(): NotificationManager {
    return remember { NotificationManager() }
}