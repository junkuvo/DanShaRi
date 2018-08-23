package junkuvo.apps.danshari.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * SharedPreferenceを保存したり取り出したりするためのシングルトンクラス
 */
public class PreferenceUtil {

    public enum PreferenceKey {
        TUTORIAL_DONE,
        SUM_UNINSTALL_COUNT,
        LAST_UNINSTALL_TIME;

        PreferenceKey() {
        }

    }

    private static SharedPreferences sharedPreferences;
    private static PreferenceUtil preferenceUtil = new PreferenceUtil();

    public static PreferenceUtil getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return preferenceUtil;
    }

    public void putString(@NonNull String key, @NonNull String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(@NonNull String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(@NonNull String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@NonNull String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putInt(@NonNull String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(@NonNull String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void putLong(@NonNull String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(@NonNull String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
