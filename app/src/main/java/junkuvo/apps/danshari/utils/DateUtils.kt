package junkuvo.apps.danshari.utils

import junkuvo.apps.danshari.data.UsageStatsData
import java.text.DateFormat
import java.text.SimpleDateFormat

fun format(usageStatsWrapper: UsageStatsData): String {
    val format = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    return format.format(usageStatsWrapper.usageStats?.lastTimeUsed)
}

