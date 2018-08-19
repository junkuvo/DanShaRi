package junkuvo.apps.danshari.view

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import junkuvo.apps.danshari.R
import junkuvo.apps.danshari.data.UsageStatsData
import junkuvo.apps.danshari.utils.format

class UsageStatsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.tv_last_used) lateinit var tvLastUsed: AppCompatTextView
    @BindView(R.id.iv_app_icon) lateinit var ivAppIcon:AppCompatImageView
    @BindView(R.id.tv_title) lateinit var tvAppTitle: AppCompatTextView
    init {
        if (itemView != null) {
            ButterKnife.bind(this, itemView)
        }
    }

    fun bindTo(usageStatsWrapper: UsageStatsData) {
        ivAppIcon.setImageDrawable(usageStatsWrapper.appIcon)
        tvAppTitle.text = usageStatsWrapper.appName
        if (usageStatsWrapper.usageStats == null) {
            tvLastUsed.setText(R.string.last_time_used_never)
        } else if (usageStatsWrapper.usageStats!!.lastTimeUsed == 0L) {
            tvLastUsed.setText(R.string.last_time_used_never)
        } else {
            tvLastUsed.text = tvAppTitle.context.getString(R.string.last_time_used,
                    format(usageStatsWrapper))
        }
    }
}