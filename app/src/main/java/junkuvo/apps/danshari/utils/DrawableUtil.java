package junkuvo.apps.danshari.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

public class DrawableUtil {
    public static GradientDrawable createGradientDrawable(Context context, int strokeWidthPx, int strokeColor, int solidColor, float radiusPx) {
        GradientDrawable ret = new GradientDrawable();

        float[] cornerRadius = new float[]{
                radiusPx, radiusPx,   // top-left x, y radius
                radiusPx, radiusPx,   // top-right x, y radius
                radiusPx, radiusPx,   // bottom-right x, y radius
                radiusPx, radiusPx,   // bottom-left x, y radius
        };

        ret.setCornerRadii(cornerRadius);
        ret.setColor(solidColor);
        ret.setStroke(strokeWidthPx, strokeColor);

        return ret;
    }

    public static StateListDrawable createStateListDrawable(Context context, int state, int strokeWidthPx, int strokeColor, int solidColor, float radiusPx) {
        StateListDrawable ret = new StateListDrawable();
        GradientDrawable gradientDrawable = DrawableUtil.createGradientDrawable(context, strokeWidthPx, strokeColor, solidColor, radiusPx);

        ret.addState(new int[]{state}, gradientDrawable);
        return ret;
    }

    public static StateListDrawable createStateListDrawableForPressed(Context context, int solidColor, float radiusPx, String pressedAlpha) {
        StateListDrawable ret = new StateListDrawable();

        GradientDrawable currentDrawable = DrawableUtil.createGradientDrawable(context, 0, ContextCompat.getColor(context, android.R.color.transparent), solidColor, radiusPx);

        int pressedColor = getColorAlpha(context, solidColor, pressedAlpha);
        GradientDrawable pressedDrawable = DrawableUtil.createGradientDrawable(context, 0, ContextCompat.getColor(context, android.R.color.transparent), pressedColor, radiusPx);

        ret.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        ret.addState(new int[]{}, currentDrawable);
        return ret;
    }

    /**
     * 色に透明度を設定する
     *
     * @param context
     * @param parentColor
     * @param alpha
     * @return
     */
    public static int getColorAlpha(Context context, int parentColor, String alpha) {
        if (parentColor == ContextCompat.getColor(context, android.R.color.transparent)) {
            return parentColor;
        }

        String hexString = Integer.toHexString(parentColor & 0x00ffffff);
        if (parentColor == ContextCompat.getColor(context, android.R.color.black)) {
            hexString = "000000";
        }

        return Color.parseColor("#"
                + alpha
                + hexString);
    }
}