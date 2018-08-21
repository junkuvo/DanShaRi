package junkuvo.apps.danshari

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.cleveroad.slidingtutorial.*
import junkuvo.apps.danshari.App.Companion.UNINSTALLER_REQUEST_CODE
import junkuvo.apps.danshari.custom_views.CustomToast
import junkuvo.apps.danshari.custom_views.Ellipsebutton
import junkuvo.apps.danshari.data.UsageStatsData
import junkuvo.apps.danshari.presenter.UsageStatsContract
import junkuvo.apps.danshari.presenter.UsageStatsPresenter
import junkuvo.apps.danshari.view.UsageStatsAdapter




class ActivityMain : AppCompatActivity(), UsageStatsContract.View {

    private val TOTAL_PAGES = 6

    override fun onUsageStatsRetrieved(list: List<UsageStatsData>) {
        showProgressBar(false)
        tvGrantAlert.visibility = GONE
        adapter.setList(list as ArrayList<UsageStatsData>)
    }

    override fun onUserHasNoPermission() {
        showProgressBar(false)
        // todo 楕円ボタンに変更
        tvGrantAlert.visibility = VISIBLE
    }

    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar
    @BindView(R.id.grant_permission_message)
    lateinit var tvGrantAlert: AppCompatTextView
    @BindView(R.id.recyclerview)
    lateinit var rvList: RecyclerView
    @BindView(R.id.b_grant_permission)
    lateinit var bGrantAlert: Ellipsebutton

    private lateinit var adapter: UsageStatsAdapter
    private lateinit var presenterUserStats: UsageStatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        rvList.layoutManager = LinearLayoutManager(this)
        adapter = UsageStatsAdapter()
        rvList.adapter = adapter

        bGrantAlert.setOnClickListener { openPermissionSettings() }

        presenterUserStats = UsageStatsPresenter(this, this)


        val mPagesColors = intArrayOf(ContextCompat.getColor(this, android.R.color.darker_gray), ContextCompat.getColor(this, android.R.color.holo_green_dark), ContextCompat.getColor(this, android.R.color.holo_red_dark), ContextCompat.getColor(this, android.R.color.holo_blue_dark), ContextCompat.getColor(this, android.R.color.holo_purple), ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        val indicatorOptions = IndicatorOptions.newBuilder(this).build()
        val tutorialPageProvider = TutorialPagesProvider()
        val tutorialOptions = TutorialFragment.newTutorialOptionsBuilder(this)
                .setUseAutoRemoveTutorialFragment(true)
                .setUseInfiniteScroll(true)
                .setPagesColors(mPagesColors)
                .setPagesCount(TOTAL_PAGES)
                .setIndicatorOptions(indicatorOptions)
                .setTutorialPageProvider(tutorialPageProvider)
                .setOnSkipClickListener(OnSkipClickListener(this))
                .build()
        val tutorialFragment = TutorialFragment.newInstance(tutorialOptions)
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, tutorialFragment)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_filter ->
                CustomToast.warning(this, "現在、開発中です。。").show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        showProgressBar(true)
        presenterUserStats.retrieveUsageStats()
    }

    private fun openPermissionSettings() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = VISIBLE
        } else {
            progressBar.visibility = GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UNINSTALLER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    private class TutorialPagesProvider : TutorialPageOptionsProvider {
         val ACTUAL_PAGES_COUNT = 3
        override fun provide(position: Int): PageOptions {
            var position = position
            @LayoutRes val pageLayoutResId: Int
            val tutorialItems: Array<TransformItem>
            position %= ACTUAL_PAGES_COUNT
            when (position) {
                0 -> {
                    pageLayoutResId = R.layout.fragment_page_first
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.LEFT_TO_RIGHT, 0.20f), TransformItem.create(R.id.ivSecondImage, Direction.RIGHT_TO_LEFT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.LEFT_TO_RIGHT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.RIGHT_TO_LEFT, 0.1f), TransformItem.create(R.id.ivFifthImage, Direction.RIGHT_TO_LEFT, 0.03f), TransformItem.create(R.id.ivSixthImage, Direction.RIGHT_TO_LEFT, 0.09f), TransformItem.create(R.id.ivSeventhImage, Direction.RIGHT_TO_LEFT, 0.14f), TransformItem.create(R.id.ivEighthImage, Direction.RIGHT_TO_LEFT, 0.07f))
                }
                1 -> {
                    pageLayoutResId = R.layout.fragment_page_third
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.20f), TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f), TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f), TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f), TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f))
                }
                2 -> {
                    pageLayoutResId = R.layout.fragment_page_second
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f), TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f), TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f), TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f), TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f), TransformItem.create(R.id.ivEighthImage, Direction.LEFT_TO_RIGHT, 0.07f))
                }
                else -> {
                    throw IllegalArgumentException("Unknown position: $position")
                }
            }

            return PageOptions.create(pageLayoutResId, position, *tutorialItems)
        }
    }

    private class OnSkipClickListener internal constructor(context: Context) : View.OnClickListener {

        private val mContext: Context

        init {
            mContext = context.applicationContext
        }

        override fun onClick(v: View) {
            Toast.makeText(mContext, "Skip button clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
