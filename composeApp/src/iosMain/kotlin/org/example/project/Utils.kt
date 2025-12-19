package org.example.project

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.timeZoneForSecondsFromGMT


actual fun formatTime(timestamp: Long): String {
   
    val date = NSDate(timeIntervalSince1970 = timestamp.toDouble() / 1000.0)

    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd/MM/yyyy HH:mm"
    
    formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)

    return formatter.stringFromDate(date)
}
