package org.example.project

import androidx.compose.runtime.Composable

expect class NotificationManager() {
    fun showNotification(title: String, message: String)
    fun requestPermission(onResult: (Boolean) -> Unit)
}

@Composable
expect fun rememberNotificationManager(): NotificationManager