package junkuvo.apps.danshari.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ad.*

class AdViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {


    fun bindTo() {
        val adRequest = PublisherAdRequest.Builder()
                .addTestDevice("EBAB8562A2BF0F81C1702F59F3E0E5C6")
                .addTestDevice("F171E99944D7E61C3B4EE10FA9DF36A8")
                .addTestDevice("3690F8088A0F2B94C318EF5D41DDFD23")
                .build()

        adView_publisher.setAdSizes(prepareAdSize(itemView.context))
        adView_publisher.loadAd(adRequest)
    }

    private fun prepareAdSize(context: Context): AdSize {
        val scale = context.resources.displayMetrics.density
        val w = context.resources.displayMetrics.widthPixels
        val h = context.resources.displayMetrics.heightPixels

        // 広告用の幅・高さ
        val adW: Int = (w / scale).toInt()
        val adH = (w / scale * 5 / 32).toInt()// バナー広告の比率
        return AdSize(adW, adH)
    }
}
