package junkuvo.apps.danshari.utils

import junkuvo.apps.danshari.data.UsageStatsData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun format(usageStatsWrapper: UsageStatsData): String {
    val format = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    return format.format(usageStatsWrapper.usageStats?.lastTimeUsed)
}

fun formatEndTime(usageStatsWrapper: UsageStatsData): String {
    val format = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    return format.format(usageStatsWrapper.usageStats?.lastTimeStamp)
}

fun calculateMonthDiff(target: Long): String {
    val calendar = Calendar.getInstance()
    val diff = calendar.timeInMillis - target

    val diffYear = diff / (TimeUnit.DAYS.toMillis(365))
    val diffMonth = diff / (TimeUnit.DAYS.toMillis(30))

    if(diffYear > 0) {
        return diffYear.toString() + "年以上使っていないアプリ"
    } else if(diffMonth > 0){
        return diffMonth.toString() + "ヶ月以上使っていないアプリ"
    }else {
        return "最近使ったアプリ"
    }
}

