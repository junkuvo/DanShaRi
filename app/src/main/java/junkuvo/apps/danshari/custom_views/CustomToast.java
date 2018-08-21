package junkuvo.apps.danshari.custom_views;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import junkuvo.apps.danshari.R;


public class CustomToast {
    /**
     * トーストを作成する
     *
     * @param context  コンテキスト
     * @param text     テキスト
     * @param duration 表示する時間
     * @return トーストのインスタンス
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        return makeText(context, text, duration, null);
    }

    /**
     * トーストを作成する
     *
     * @param context  コンテキスト
     * @param text     表示するテキスト
     * @param duration 表示する時間
     * @param resId    表示するアイコン
     * @return トーストのインスタンス
     */
    public static Toast makeText(Context context, CharSequence text, int duration, @DrawableRes Integer resId) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.custom_toast, null);
        ((TextView) v.findViewById(R.id.tv_toast_text)).setText(text);
        if (resId != null) {
            AppCompatImageView ivToastIcon = v.findViewById(R.id.iv_toast_icon);
            ivToastIcon.setVisibility(View.VISIBLE);
            ivToastIcon.setImageResource(resId);
        } else {
            AppCompatImageView ivToastIcon = v.findViewById(R.id.iv_toast_icon);
            ivToastIcon.setVisibility(View.GONE);
        }
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(v);
        return toast;
    }

    public static Toast success(Context context, CharSequence text, @DrawableRes Integer resId) {
        Toast toast = makeText(context, text, Toast.LENGTH_LONG, resId);
        toast.getView().findViewById(R.id.cl_toast).setBackground(ContextCompat.getDrawable(context, R.drawable.background_toast_success));
        return toast;
    }

    public static Toast warning(Context context, CharSequence text, @DrawableRes Integer resId) {
        Toast toast = makeText(context, text, Toast.LENGTH_LONG, resId);
        toast.getView().findViewById(R.id.cl_toast).setBackground(ContextCompat.getDrawable(context, R.drawable.background_toast_warning));
        return toast;
    }

    public static Toast success(Context context, CharSequence text) {
        Toast toast = makeText(context, text, Toast.LENGTH_LONG, R.drawable.ic_done_white_36dp);
        toast.getView().findViewById(R.id.cl_toast).setBackground(ContextCompat.getDrawable(context, R.drawable.background_toast_success));
        return toast;
    }

    public static Toast warning(Context context, CharSequence text) {
        Toast toast = makeText(context, text, Toast.LENGTH_LONG, R.drawable.ic_error_outline_white_36dp);
        toast.getView().findViewById(R.id.cl_toast).setBackground(ContextCompat.getDrawable(context, R.drawable.background_toast_warning));
        return toast;
    }
}
