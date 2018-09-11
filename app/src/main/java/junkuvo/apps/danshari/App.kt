package junkuvo.apps.danshari

import android.app.Application
import com.google.android.gms.ads.MobileAds
import junkuvo.apps.danshari.utils.FirebaseEventUtil


class App : Application() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val UNINSTALLER_REQUEST_CODE = 1
    }

    override fun onCreate() {
        super.onCreate()
//        Fabric.with(this, Crashlytics())
        MobileAds.initialize(this, "ca-app-pub-1630604043812019~3470457799")

        FirebaseEventUtil.initFirebase(this)

    }
}