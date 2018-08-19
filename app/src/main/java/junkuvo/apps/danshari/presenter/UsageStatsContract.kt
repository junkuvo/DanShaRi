package junkuvo.apps.danshari.presenter

import junkuvo.apps.danshari.data.UsageStatsData

interface UsageStatsContract {
    interface View {
        fun onUsageStatsRetrieved(list: List<UsageStatsData>)
        fun onUserHasNoPermission()
    }

    interface Presenter {
        fun retrieveUsageStats()
    }
}