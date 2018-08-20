package junkuvo.apps.danshari.view

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import junkuvo.apps.danshari.ActivityMain
import junkuvo.apps.danshari.App.Companion.UNINSTALLER_REQUEST_CODE
import junkuvo.apps.danshari.R
import junkuvo.apps.danshari.data.UsageStatsData
import junkuvo.apps.danshari.utils.format
import junkuvo.apps.danshari.utils.formatEndTime


class UsageStatsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.tv_last_used)
    lateinit var tvLastUsed: AppCompatTextView
    @BindView(R.id.iv_app_icon)
    lateinit var ivAppIcon: AppCompatImageView
    @BindView(R.id.tv_title)
    lateinit var tvAppTitle: AppCompatTextView

    var packageName: String = ""


    init {
        if (itemView != null) {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener {
                if (packageName.isNotEmpty()) {
                    val uri = Uri.fromParts("package", packageName, null)
                    val intent = Intent(Intent.ACTION_DELETE, uri)
                    val activityMain: ActivityMain = itemView.context as ActivityMain
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                    activityMain.startActivityForResult(intent, UNINSTALLER_REQUEST_CODE)
                } else {
                    // todo toast
                }
            }
        }
    }

    fun bindTo(usageStatsWrapper: UsageStatsData) {
        packageName = usageStatsWrapper.usageStats?.packageName ?: ""
        ivAppIcon.setImageDrawable(usageStatsWrapper.appIcon)
        tvAppTitle.text = usageStatsWrapper.appName
        if (usageStatsWrapper.usageStats == null) {
            tvLastUsed.setText(R.string.last_time_used_never)
        } else if (usageStatsWrapper.usageStats!!.lastTimeUsed == 0L) {
            tvLastUsed.text = tvAppTitle.context.getString(R.string.last_time_used,
                    formatEndTime(usageStatsWrapper))
        } else {
            tvLastUsed.text = tvAppTitle.context.getString(R.string.last_time_used,
                    format(usageStatsWrapper))
        }
        Log.d("okubookubo", usageStatsWrapper.usageStats?.totalTimeInForeground.toString() + ":"
                + usageStatsWrapper.usageStats?.packageName + usageStatsWrapper.appName
                + (usageStatsWrapper.usageStats?.lastTimeUsed ?: "null")
                + (usageStatsWrapper.usageStats?.lastTimeStamp ?: "null"))
    }
}