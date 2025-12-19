package org.example.project

import java.text.SimpleDateFormat
import java.util.*


actual fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
