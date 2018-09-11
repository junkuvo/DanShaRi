package junkuvo.apps.danshari.data

import android.app.usage.UsageStats
import android.graphics.drawable.Drawable

class UsageStatsData( var usageStats: UsageStats?, var appIcon: Drawable?, var appName: String) : Comparable<UsageStatsData> {

    var previousTime: Long = 0

    constructor() : this(null, null, "")

    override fun compareTo(other: UsageStatsData): Int {
        return if (usageStats == null && other.usageStats != null) {
            1
        } else if (other.usageStats == null && usageStats != null) {
            -1
        } else if (other.usageStats == null && usageStats == null) {
            0
        } else {
            java.lang.Long.compare(other.usageStats!!.lastTimeUsed, usageStats!!.lastTimeUsed)
        }
    }
}