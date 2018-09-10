package junkuvo.apps.danshari.view

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import junkuvo.apps.danshari.R


class AdViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    var packageName: String = ""
    @BindView(R.id.adView)
    lateinit var mAdView: AdView
    @BindView(R.id.cl_ad)
    lateinit var clAd: ConstraintLayout

    init {
        if (itemView != null) {
            ButterKnife.bind(this, itemView)
        }
    }

    fun bindTo() {
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        clAd.setOnClickListener { mAdView.performClick() }
    }
}