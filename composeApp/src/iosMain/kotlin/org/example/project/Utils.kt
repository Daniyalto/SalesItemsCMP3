package org.example.project

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.timeZoneForSecondsFromGMT

// ACTUAL: iOS implementering.
actual fun formatTime(timestamp: Long): String {
    // Konverterer Long (millisekunder) til timeIntervalSince1970 (sekunder)
    val date = NSDate(timeIntervalSince1970 = timestamp.toDouble() / 1000.0)

    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd/MM/yyyy HH:mm"

    // Anvender typisk UTC+0 for ensartethed, men man kan justere til lokal tid
    formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)

    return formatter.stringFromDate(date)
}