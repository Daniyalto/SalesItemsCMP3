package org.example.project

actual fun formatTime(timestamp: Long): String {
    return js("new Date(timestamp).toLocaleString()") as String
}
