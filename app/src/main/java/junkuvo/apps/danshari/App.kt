package junkuvo.apps.danshari

import android.app.Application


class App : Application() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val UNINSTALLER_REQUEST_CODE = 1
    }

    override fun onCreate() {
        super.onCreate()
//        Fabric.with(this, Crashlytics())
    }
}