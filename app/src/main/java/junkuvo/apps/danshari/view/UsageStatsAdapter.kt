package junkuvo.apps.danshari.view

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import junkuvo.apps.danshari.R
import junkuvo.apps.danshari.data.UsageStatsData

class UsageStatsAdapter() : RecyclerView.Adapter<UsageStatsViewHolder>() {

    init {
        setHasStableIds(true)
    }

    //    var results: ArrayList<UsageStatsData> = ArrayList()
    var results: HashMap<String?, UsageStatsData> = HashMap()
    var resultList: ArrayList<UsageStatsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageStatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage_stat, parent, false)
        return UsageStatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Between execution of left != null and queue.add(left) another thread could have changed the value of left to null.
        // @see https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha
        return results.size
    }

    override fun onBindViewHolder(holder: UsageStatsViewHolder, position: Int) {
        if (position != 0) {
            val previous = resultList[position - 1].usageStats?.lastTimeUsed ?: 0
            resultList[position].previousTime = previous
        }
        holder.bindTo(resultList[position])
    }

    fun setList(list: ArrayList<UsageStatsData>) {
        val map = list.associateBy({ it.usageStats?.packageName }, { it })
        this.results.putAll(map)
        resultList = ArrayList(results.values.sortedBy { it.usageStats?.lastTimeUsed })
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return resultList[position].hashCode().toLong()
    }

    fun remove(packageName: String) {
        val usageStatsData = results[packageName]
        val position = resultList.indexOf(usageStatsData)
        results.remove(packageName)
        resultList.removeAt(position)
        notifyItemRemoved(position)
    }
}