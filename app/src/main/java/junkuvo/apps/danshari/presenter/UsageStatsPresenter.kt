package junkuvo.apps.danshari.presenter

import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process.myUid
import android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED
import junkuvo.apps.danshari.BuildConfig
import junkuvo.apps.danshari.data.UsageStatsData
import java.util.*

class UsageStatsPresenter(private val view: UsageStatsContract.View, private val context: Context)
    : UsageStatsContract.Presenter {

    private val usageStatsManager: UsageStatsManager = context.getSystemService("usagestats") as UsageStatsManager
    private val packageManager: PackageManager = context.packageManager

    private val flags = PackageManager.GET_META_DATA
//    or PackageManager.GET_SHARED_LIBRARY_FILES
//    or PackageManager.GET_UNINSTALLED_PACKAGES

    override fun retrieveUsageStats() {
        if (!checkForPermission(context)) {
            view.onUserHasNoPermission()
            return
        }

        val installedApps = getInstalledAppList()
        val usageStats = usageStatsManager.queryUsageStats(
                // INTERVAL_DAILYで1日単位で一度でも使われているアプリを取得する
                UsageStatsManager.INTERVAL_MONTHLY, getStartTime(), System.currentTimeMillis())
//        val usageStats = usageStatsManager.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis())
        val stats = ArrayList<UsageStats>()
        stats.addAll(usageStats)

        val finalList = buildUsageStatsWrapper(installedApps, stats)
        view.onUsageStatsRetrieved(finalList)
    }

    private fun getStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -4)
        return calendar.timeInMillis
    }

    private fun getInstalledAppList(): List<String> {
        val infos = packageManager.getInstalledApplications(flags)
        val installedApps = ArrayList<String>()
        for (info in infos) {
            installedApps.add(info.packageName)
        }
        installedApps.remove(BuildConfig.APPLICATION_ID)// このアプリ抜く
        return installedApps
    }

    private fun checkForPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.packageName)
        return mode == MODE_ALLOWED
    }

    private fun buildUsageStatsWrapper(packageNames: List<String>, usageStatses: List<UsageStats>): List<UsageStatsData> {
        val list = ArrayList<UsageStatsData>()
        for (name in packageNames) {
            var added = false
            for (stat in usageStatses) {
                if (name == stat.packageName) {
                    added = true
                    // todo 最新の記録だけをadd
                    if (!name.contains("com.google.") && !name.contains("com.android.") && name != "android") {
                        // com.android. か com.google. がpackage nameに含まれたらremove
                        if(stat.lastTimeUsed > 0) {
                            list.add(fromUsageStat(stat))
                        }
                    }
                }
            }
//            if (!added) {
//                // 新しく登場したアプリ(usageStatsが取得できないアプリ)
//                list.add(fromUsageStat(name))
//            }
        }
        // todo 使用時間短い順　最終起動時間古い順
        list.sort()
        return list
    }

    @Throws(IllegalArgumentException::class)
    private fun fromUsageStat(packageName: String): UsageStatsData {
        try {
            val ai = packageManager.getApplicationInfo(packageName, 0)
            return UsageStatsData(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString())

        } catch (e: PackageManager.NameNotFoundException) {
            throw IllegalArgumentException(e)
        }

    }

    @Throws(IllegalArgumentException::class)
    private fun fromUsageStat(usageStats: UsageStats): UsageStatsData {
        try {
            val ai = packageManager.getApplicationInfo(usageStats.packageName, 0)
            return UsageStatsData(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString())

        } catch (e: PackageManager.NameNotFoundException) {
            throw IllegalArgumentException(e)
        }
    }
}