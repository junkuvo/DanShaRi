package junkuvo.apps.danshari.utils;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventUtil {

    /**
     * Firebaseのデフォルトのイベント送信機能も利用するため、
     */
    private static FirebaseAnalytics firebaseAnalytics;

    public static FirebaseAnalytics getInstance() {
        return firebaseAnalytics;
    }

    public static void initFirebase(Application application) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(application);
        }
    }

    /**
     * Firebaseへ送信するイベントをまとめた列挙体<br/>
     */
    public enum FirebaseEvent {
        // イベント名
        APP_UNINSTALL("app_uninstall"),
        PERMISSION_SETTING("permission_setting"),
        TUTORIAL_PROCESS("tutorial_process"),// チュートリアルどこまで行ったか。
        PUSH_NOTIFICATION_OPEN("push_notification_open"),;

        private String eventName;

        FirebaseEvent(String eventName) {
            this.eventName = eventName;
        }

        /**
         * @return イベント用の画面名
         */
        public String getEventName() {
            return eventName;
        }
    }

    /**
     * イベントに付与して送信するBundleデータ用のキーをまとめた列挙体
     */
    public enum FirebaseEventParamKey {
        UNINSTALL_TYPE("uninstall_type"),
        PERMISSION_PROCESS_NAME("permission_process_name"),
        TUTORIAL_PROCESS_NAME("tutorial_process_name"),;

        private String key;

        FirebaseEventParamKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }


    /**
     * FirebaseAnalytics向けにイベント送信するメソッド<br/>
     * 本番のデバッグモード以外は送信しています。
     *
     * @param firebaseEvent
     * @param bundle
     */
    public static void sendFirebaseEvent(FirebaseEvent firebaseEvent, @Nullable Bundle bundle) {
//        if (!(BuildConfig.DEBUG)) {
            // デバッグモードの本番ビルド以外はイベント送信する
            firebaseAnalytics.logEvent(firebaseEvent.getEventName(), bundle);
//        }
    }


    public final static String TUTORIAL_PROCESS_START = "TUTORIAL_PROCESS_START";
    public final static String TUTORIAL_PROCESS_2 = "TUTORIAL_PROCESS_2";
    public final static String TUTORIAL_PROCESS_3 = "TUTORIAL_PROCESS_3";
    public final static String TUTORIAL_PROCESS_4 = "TUTORIAL_PROCESS_4";
    public final static String TUTORIAL_PROCESS_SKIP = "TUTORIAL_PROCESS_SKIP";

    public static void sendTutorialProcess(String tutorialPhase) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseEventParamKey.TUTORIAL_PROCESS_NAME.getKey(), tutorialPhase);
        sendFirebaseEvent(FirebaseEvent.TUTORIAL_PROCESS, bundle);
    }

    public final static String PERMISSION_PROCESS_OPEN = "PERMISSION_PROCESS_OPEN";

    public static void sendPermissionProcess(String phase) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseEventParamKey.PERMISSION_PROCESS_NAME.getKey(), phase);
        sendFirebaseEvent(FirebaseEvent.PERMISSION_SETTING, bundle);
    }

    public final static String UNINSTALL_TYPE_CLEAR = "UNINSTALL_TYPE_CLEAR";
    public final static String UNINSTALL_TYPE_UNINSTALLED = "UNINSTALL_TYPE_UNINSTALLED";

    public static void sendUninstall(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseEventParamKey.UNINSTALL_TYPE.getKey(), type);
        sendFirebaseEvent(FirebaseEvent.APP_UNINSTALL, bundle);
    }
}
