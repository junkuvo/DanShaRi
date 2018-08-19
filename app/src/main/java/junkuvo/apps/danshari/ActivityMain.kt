package junkuvo.apps.danshari

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import junkuvo.apps.danshari.data.UsageStatsData
import junkuvo.apps.danshari.presenter.UsageStatsContract
import junkuvo.apps.danshari.presenter.UsageStatsPresenter
import junkuvo.apps.danshari.view.UsageStatsAdapter

class ActivityMain : AppCompatActivity(), UsageStatsContract.View {
    override fun onUsageStatsRetrieved(list: List<UsageStatsData>) {
        showProgressBar(false)
        tvGrantAlert.visibility = GONE
        adapter.setList(list as ArrayList<UsageStatsData>)
    }

    override fun onUserHasNoPermission() {
        showProgressBar(false)
        tvGrantAlert.visibility = VISIBLE
    }

    @BindView(R.id.progress_bar) lateinit var progressBar: ProgressBar
    @BindView(R.id.grant_permission_message) lateinit var  tvGrantAlert:AppCompatTextView
    @BindView(R.id.recyclerview) lateinit var rvList: RecyclerView

    private lateinit var adapter: UsageStatsAdapter
    private lateinit var presenterUserStats:UsageStatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        rvList.layoutManager = LinearLayoutManager(this)
        adapter = UsageStatsAdapter()
        rvList.adapter = adapter

        tvGrantAlert.setOnClickListener({ openPermissionSettings() })

        presenterUserStats = UsageStatsPresenter(this, this)
    }

    override fun onResume() {
        super.onResume()
        showProgressBar(true)
        presenterUserStats.retrieveUsageStats()
    }

    private fun  openPermissionSettings(){
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = VISIBLE
        } else {
            progressBar.visibility = GONE
        }
    }

}
