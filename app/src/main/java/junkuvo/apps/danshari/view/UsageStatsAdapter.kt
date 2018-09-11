package junkuvo.apps.danshari.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import junkuvo.apps.danshari.R
import junkuvo.apps.danshari.data.UsageStatsData

class UsageStatsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    //    var results: ArrayList<UsageStatsData> = ArrayList()
    var results: HashMap<String?, UsageStatsData> = HashMap()
    var resultList: ArrayList<UsageStatsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage_stat, parent, false)
                UsageStatsViewHolder(view)
            }
            VIEW_TYPE_AD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ad, parent, false)
                AdViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage_stat, parent, false)
                UsageStatsViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return when (results.size) {
            0, 1 -> {
                // Between execution of left != null and queue.add(left) another thread could have changed the value of left to null.
                // @see https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha
                results.size
            }
            else -> {
                results.size + 1
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != 0) {
            if (position == AD_POSITION) {
                // ad do nothing
            } else if (position == AD_POSITION + 1) {
                val previous = resultList[1].usageStats?.lastTimeUsed ?: 0
                resultList[position].previousTime = previous
            } else {
                val previous = resultList[position - 1].usageStats?.lastTimeUsed ?: 0
                resultList[position].previousTime = previous
            }
        }

        if (position == AD_POSITION) {
            // do nothing
            (holder as AdViewHolder).bindTo()
        } else {
            (holder as UsageStatsViewHolder).bindTo(resultList[position])
        }
    }

    fun setList(list: ArrayList<UsageStatsData>) {
        val map = list.associateBy({ it.usageStats?.packageName }, { it })
        this.results.putAll(map)
        resultList = ArrayList(results.values.sortedBy { it.usageStats?.lastTimeUsed })
        notifyDataSetChanged()
    }

    fun addAdView(){
        resultList.add(AD_POSITION, UsageStatsData())
        notifyItemInserted(AD_POSITION)
    }

    override fun getItemId(position: Int): Long {
        if (position == AD_POSITION) {
            return 0 // ad id
        }
        return resultList[position].hashCode().toLong()
    }

    fun remove(packageName: String) {
        val usageStatsData = results[packageName]
        val position = resultList.indexOf(usageStatsData)
        results.remove(packageName)
        resultList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == AD_POSITION) {
            return VIEW_TYPE_AD
        }
        return VIEW_TYPE_ITEM
    }

    final val AD_POSITION = 2// 0,1,2の2
    final val VIEW_TYPE_ITEM: Int = 0
    final val VIEW_TYPE_AD: Int = 1
}