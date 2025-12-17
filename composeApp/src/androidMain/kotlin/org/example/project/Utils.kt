package org.example.project

import java.text.SimpleDateFormat
import java.util.*

// ACTUAL: Android implementering.
actual fun formatTime(timestamp: Long): String {
    // Bemærk: timestamp er allerede i millisekunder, da vi brugte System.currentTimeMillis() i SaleItem.
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}