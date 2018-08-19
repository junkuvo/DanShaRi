package junkuvo.apps.danshari.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import junkuvo.apps.danshari.R
import junkuvo.apps.danshari.data.UsageStatsData

class UsageStatsAdapter() : RecyclerView.Adapter<UsageStatsViewHolder>() {


    var results: ArrayList<UsageStatsData>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageStatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usage_stat_item, parent, false)
        return UsageStatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Between execution of left != null and queue.add(left) another thread could have changed the value of left to null.
        // @see https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha
        return results?.size?:0
    }

    override fun onBindViewHolder(holder: UsageStatsViewHolder, position: Int) {
        holder.bindTo(results?.get(position)?:return)
    }

    fun setList(list: ArrayList<UsageStatsData>) {
        this.results = list
        notifyDataSetChanged()
    }
}