package junkuvo.apps.danshari

import android.app.Activity
import android.app.usage.UsageStatsManager
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
import android.widget.FrameLayout
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.cleveroad.slidingtutorial.*
import junkuvo.apps.danshari.App.Companion.PERMISSION_REQUEST_CODE
import junkuvo.apps.danshari.App.Companion.UNINSTALLER_REQUEST_CODE
import junkuvo.apps.danshari.custom_views.CustomToast
import junkuvo.apps.danshari.custom_views.Ellipsebutton
import junkuvo.apps.danshari.data.UsageStatsData
import junkuvo.apps.danshari.presenter.UsageStatsContract
import junkuvo.apps.danshari.presenter.UsageStatsPresenter
import junkuvo.apps.danshari.utils.PreferenceUtil
import junkuvo.apps.danshari.utils.PreferenceUtil.PreferenceKey.*
import junkuvo.apps.danshari.view.UsageStatsAdapter


class ActivityMain : AppCompatActivity(), UsageStatsContract.View {

    private var hasPermission = false

    override fun onUsageStatsRetrieved(list: List<UsageStatsData>) {
        hasPermission = true
        showProgressBar(false)
        tvGrantAlert.visibility = GONE
        bGrantAlert.visibility = GONE
        adapter.setList(list as ArrayList<UsageStatsData>)
    }

    override fun onUserHasNoPermission() {
        showProgressBar(false)
        tvGrantAlert.visibility = VISIBLE
        bGrantAlert.visibility = VISIBLE
    }

    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar
    @BindView(R.id.grant_permission_message)
    lateinit var tvGrantAlert: AppCompatTextView
    @BindView(R.id.recyclerview)
    lateinit var rvList: RecyclerView
    @BindView(R.id.b_grant_permission)
    lateinit var bGrantAlert: Ellipsebutton
    @BindView(R.id.container)
    lateinit var flTutorial: FrameLayout

    var uninstallingPackageName = ""


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

        if (!PreferenceUtil.getInstance(this).getBoolean(TUTORIAL_DONE.name)) {
            startTutorial()
        }

        showProgressBar(true)
        // results.putAllが後勝ちなので一応この順番。
        presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_MONTHLY)
        presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_WEEKLY)
        presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_DAILY)
    }

    private fun startTutorial() {
        // tutorial
        val mPagesColors = intArrayOf(ContextCompat.getColor(this, android.R.color.darker_gray),
                ContextCompat.getColor(this, android.R.color.holo_green_dark),
//                ContextCompat.getColor(this, android.R.color.holo_red_dark),
                ContextCompat.getColor(this, android.R.color.holo_blue_dark),
//                ContextCompat.getColor(this, android.R.color.holo_purple),
                ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        val indicatorOptions = IndicatorOptions.newBuilder(this).build()
        val tutorialPageProvider = TutorialPagesProvider()
        val tutorialOptions = TutorialFragment.newTutorialOptionsBuilder(this)
                .setUseAutoRemoveTutorialFragment(true)
                .setUseInfiniteScroll(true)
                .setPagesColors(mPagesColors)
                .setPagesCount(mPagesColors.size)
                .setIndicatorOptions(indicatorOptions)
                .setTutorialPageProvider(tutorialPageProvider)
                .setOnSkipClickListener(OnSkipClickListener(this, flTutorial))
                .build()
        val tutorialFragment = TutorialFragment.newInstance(tutorialOptions)
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, tutorialFragment)
                .commit()
    }

    public fun onClick(view: View){
        openPermissionSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_filter -> {
                CustomToast.warning(this, "現在、開発中です。。").show()

                if (BuildConfig.DEBUG) {
                    PreferenceUtil.getInstance(this).clear(TUTORIAL_DONE.name)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openPermissionSettings() {
        startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), PERMISSION_REQUEST_CODE)
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
                val count = PreferenceUtil.getInstance(this).getInt(SUM_UNINSTALL_COUNT.name)
                PreferenceUtil.getInstance(this).putLong(LAST_UNINSTALL_TIME.name, System.currentTimeMillis())
                PreferenceUtil.getInstance(this).putInt(SUM_UNINSTALL_COUNT.name, count + 1)
                adapter.remove(uninstallingPackageName)
                CustomToast.success(this, "🎉断捨離成功🙌").show()
            }
        }else if(requestCode == PERMISSION_REQUEST_CODE) {
            // results.putAllが後勝ちなので一応この順番。
            presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_MONTHLY)
            presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_WEEKLY)
            presenterUserStats.retrieveUsageStats(UsageStatsManager.INTERVAL_DAILY)
        }
    }

    private class TutorialPagesProvider : TutorialPageOptionsProvider {
        override fun provide(position: Int): PageOptions {
            var position = position
            @LayoutRes val pageLayoutResId: Int
            val tutorialItems: Array<TransformItem>
            when (position) {
                0 -> {
                    pageLayoutResId = R.layout.fragment_page_first
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.LEFT_TO_RIGHT, 0.20f), TransformItem.create(R.id.ivSecondImage, Direction.RIGHT_TO_LEFT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.LEFT_TO_RIGHT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.RIGHT_TO_LEFT, 0.1f), TransformItem.create(R.id.ivFifthImage, Direction.RIGHT_TO_LEFT, 0.03f), TransformItem.create(R.id.ivSixthImage, Direction.RIGHT_TO_LEFT, 0.09f), TransformItem.create(R.id.ivSeventhImage, Direction.RIGHT_TO_LEFT, 0.14f), TransformItem.create(R.id.ivEighthImage, Direction.RIGHT_TO_LEFT, 0.07f))
                }
                1 -> {
                    pageLayoutResId = R.layout.fragment_page_third
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.20f), TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f), TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f), TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f), TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f))
                }
                2 -> {
                    pageLayoutResId = R.layout.fragment_page_fourth
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f), TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f), TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f), TransformItem.create(R.id.ivEighthImage, Direction.LEFT_TO_RIGHT, 0.07f))
                }
                3 -> {
                    pageLayoutResId = R.layout.fragment_page_last
                    tutorialItems = arrayOf(TransformItem.create(R.id.ivFirstImage, Direction.LEFT_TO_RIGHT, 0.20f), TransformItem.create(R.id.ivSecondImage, Direction.RIGHT_TO_LEFT, 0.06f), TransformItem.create(R.id.ivThirdImage, Direction.LEFT_TO_RIGHT, 0.08f), TransformItem.create(R.id.ivFourthImage, Direction.RIGHT_TO_LEFT, 0.1f), TransformItem.create(R.id.ivSeventhImage, Direction.RIGHT_TO_LEFT, 0.14f), TransformItem.create(R.id.ivEighthImage, Direction.RIGHT_TO_LEFT, 0.07f))
                }
                else -> {
                    throw IllegalArgumentException("Unknown position: $position")
                }
            }

            return PageOptions.create(pageLayoutResId, position, *tutorialItems)
        }
    }

    private class OnSkipClickListener internal constructor(context: Context, container: FrameLayout) : View.OnClickListener {

        private val mContext: Context
        private val flTutorial: FrameLayout

        init {
            mContext = context.applicationContext
            flTutorial = container
        }

        override fun onClick(v: View) {
            flTutorial.visibility = GONE
            PreferenceUtil.getInstance(mContext).putBoolean(TUTORIAL_DONE.name, true)
        }
    }
}
