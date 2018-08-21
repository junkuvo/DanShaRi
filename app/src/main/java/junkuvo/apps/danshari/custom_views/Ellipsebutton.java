package junkuvo.apps.danshari.custom_views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import junkuvo.apps.danshari.R;
import junkuvo.apps.danshari.utils.DrawableUtil;


/**
 * 楕円(ぽいやつ) を実現するための汎用型クラス
 */
public class Ellipsebutton extends LinearLayout {
    private AppCompatTextView buttonTextView;
    private AppCompatImageView symbolImageView;
    private int solidColor = 0;
    private int strokeWidthPx = 0;
    private int strokeColor = 0;
    private int srcColor = 0;
    private boolean hasPressedInteraction = true;   // true：Pressした時の見た目の変化が有効、false：無効
    private float cornerRadius[];

    public Ellipsebutton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public Ellipsebutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public Ellipsebutton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Ellipsebutton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        buttonTextView = new AppCompatTextView(context);
        symbolImageView = new AppCompatImageView(context);

        // Load attributes
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.EllipseButton, defStyleAttr, defStyleRes);

        StateListDrawable gradientDrawable = new StateListDrawable();
        GradientDrawable currentDrawable = new GradientDrawable();
        GradientDrawable pressedDrawable = new GradientDrawable();
        GradientDrawable enabledDrawable = new GradientDrawable();

        if (a.hasValue(R.styleable.EllipseButton_webRadius)) {
            int radius = a.getDimensionPixelSize(R.styleable.EllipseButton_webRadius, 0);
            cornerRadius = new float[]{
                    radius, radius,   // top-left x, y radius
                    radius, radius,   // top-right x, y radius
                    radius, radius,   // bottom-right x, y radius
                    radius, radius,   // bottom-left x, y radius
            };
        } else {
            boolean existRadiusTL = a.hasValue(R.styleable.EllipseButton_webTopLeftRadius);
            boolean existRadiusTR = a.hasValue(R.styleable.EllipseButton_webTopRightRadius);
            boolean existRadiusBL = a.hasValue(R.styleable.EllipseButton_webBottomLeftRadius);
            boolean existRadiusBR = a.hasValue(R.styleable.EllipseButton_webBottomRightRadius);
            if (existRadiusTL && existRadiusTR && existRadiusBL && existRadiusBR) {
                cornerRadius = new float[]{
                        a.getDimensionPixelSize(R.styleable.EllipseButton_webTopLeftRadius, 0), a.getDimensionPixelSize(R.styleable.EllipseButton_webTopLeftRadius, 0),   // top-left x, y radius
                        a.getDimensionPixelSize(R.styleable.EllipseButton_webTopRightRadius, 0), a.getDimensionPixelSize(R.styleable.EllipseButton_webTopRightRadius, 0),   // top-right x, y radius
                        a.getDimensionPixelSize(R.styleable.EllipseButton_webBottomRightRadius, 0), a.getDimensionPixelSize(R.styleable.EllipseButton_webBottomRightRadius, 0),   // bottom-right x, y radius
                        a.getDimensionPixelSize(R.styleable.EllipseButton_webBottomLeftRadius, 0), a.getDimensionPixelSize(R.styleable.EllipseButton_webBottomLeftRadius, 0),   // bottom-left x, y radius
                };
            } else {
                // とりあえず大きな値を設定しておく
                cornerRadius = new float[]{
                        1000, 1000,
                        1000, 1000,
                        1000, 1000,
                        1000, 1000,
                };
            }
        }

        if (cornerRadius != null) {
            currentDrawable.setCornerRadii(cornerRadius);
            pressedDrawable.setCornerRadii(cornerRadius);
            enabledDrawable.setCornerRadii(cornerRadius);
        }

        solidColor = a.getColor(R.styleable.EllipseButton_webSolidColor, ContextCompat.getColor(context, android.R.color.transparent));
        strokeColor = a.getColor(R.styleable.EllipseButton_webStrokeColor, ContextCompat.getColor(context, android.R.color.transparent));

        currentDrawable.setColor(solidColor);
        pressedDrawable.setColor(solidColor);
        enabledDrawable.setColor(getColorAlpha(context, solidColor, context.getString(R.string.color_alpha_50)));

        strokeWidthPx = getResources().getDimensionPixelSize(R.dimen.basic_border_line_width);
        if (a.hasValue(R.styleable.EllipseButton_webStrokeWidth)) {
            strokeWidthPx = a.getDimensionPixelSize(R.styleable.EllipseButton_webStrokeWidth, strokeWidthPx);
        }
        currentDrawable.setStroke(strokeWidthPx, strokeColor);

        int textColor = a.getColor(R.styleable.EllipseButton_webTextColor, buttonTextView.getCurrentTextColor());
        int textPressedColor = ContextCompat.getColor(context, android.R.color.white);

        hasPressedInteraction = a.getBoolean(R.styleable.EllipseButton_webIsPressedInteraction, true);
        // 押されたインタラクションフラグがTrue（有効）の場合、インタラクションの色を設定する
        if (hasPressedInteraction) {
            // 背景色が透明　かつ　枠色が透明ではない場合、押したテキストの色と枠色に５０％透明度を設定する
            if ((solidColor == ContextCompat.getColor(context, android.R.color.transparent) || solidColor == ContextCompat.getColor(context, android.R.color.white)) &&
                    strokeColor != ContextCompat.getColor(context, android.R.color.transparent)) {
                textPressedColor = getColorAlpha(context, textColor, context.getString(R.string.color_alpha_50));
                pressedDrawable.setStroke(strokeWidthPx, getColorAlpha(context, strokeColor, context.getString(R.string.color_alpha_50)));

                // 上記以外の場合、背景色に７０％透明度を設定する
            } else {
                pressedDrawable.setColor(getColorAlpha(context, solidColor, context.getString(R.string.color_alpha_70)));
            }

            gradientDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        }

        gradientDrawable.addState(new int[]{-android.R.attr.state_enabled}, enabledDrawable);
        gradientDrawable.addState(new int[]{}, currentDrawable);

        setBackgroundDrawable(gradientDrawable);

        if (a.hasValue(R.styleable.EllipseButton_webText)) {
            setText(a.getString(R.styleable.EllipseButton_webText));
        }

        if (a.hasValue(R.styleable.EllipseButton_webTextSize)) {
            setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    a.getDimensionPixelSize(R.styleable.EllipseButton_webTextSize, context.getResources().getDimensionPixelSize(R.dimen.basic_fix_fontsize)));
        }

        int textDisableColor = textColor;
        boolean isPressedInteractionTextColor = a.getBoolean(R.styleable.EllipseButton_webIsPressedInteractionTextColor, false);
        // 押されたインタラクション時のテキスト色フラグがtrue（有効）の場合、
        // 押された：テキスト色の透明度７０％を設定、非活性：テキスト色の透明度を５０％を設定
        if (isPressedInteractionTextColor) {
            textPressedColor = getColorAlpha(context, textColor, context.getString(R.string.color_alpha_70));
            textDisableColor = getColorAlpha(context, textColor, context.getString(R.string.color_alpha_50));
        }

        // 押されたインタラクションフラグがFalse（無効）または　押された時のテキストに設定されている色が白の場合
        if (!hasPressedInteraction || textPressedColor == ContextCompat.getColor(context, android.R.color.white)) {
            buttonTextView.setTextColor(textColor);

        } else {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_pressed},
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{},
                    },
                    new int[]{
                            textPressedColor,
                            textDisableColor,
                            textColor,
                    }
            );
            buttonTextView.setTextColor(colorStateList);
        }

        if (a.hasValue(R.styleable.EllipseButton_src)) {
            setSrc(a.getResourceId(R.styleable.EllipseButton_src, 0));
            symbolImageView.setVisibility(View.VISIBLE);
        } else {
            symbolImageView.setVisibility(View.GONE); // シンボルが設定されてない場合は、表示しない
        }

        if (a.hasValue(R.styleable.EllipseButton_srcColor)) {
            srcColor = a.getColor(R.styleable.EllipseButton_srcColor, 0);
            setSrcColor(srcColor);
        }

        if (a.hasValue(R.styleable.EllipseButton_webBold)) {
            if (a.getBoolean(R.styleable.EllipseButton_webBold, false)) {
                buttonTextView.setTypeface(null, Typeface.BOLD);
            }
        }

        a.recycle();

        setGravity(Gravity.CENTER);
        LayoutParams symbolParams =
                new LayoutParams(getResources().getDimensionPixelSize(R.dimen.basic_icon_width_s2),
                        getResources().getDimensionPixelSize(R.dimen.basic_icon_height_s2));
        symbolParams.setMargins(0, 0, getResources().getDimensionPixelOffset(R.dimen.basic_blank_s), 0); // シンボルとテキストの間のマージンを設定する
        addView(symbolImageView, symbolParams);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(buttonTextView, params);

    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        // 押されたインタラクションフラグがTrue（有効）かつ アイコンが表示されている場合、ボタンを押したらアイコンの色を切り替える処理を行う
        if (hasPressedInteraction && symbolImageView.getVisibility() == View.VISIBLE && srcColor != 0) {
            if (isPressed()) {
                symbolImageView.setColorFilter(getColorAlpha(getContext(), srcColor, getContext().getString(R.string.color_alpha_50)), PorterDuff.Mode.SRC_IN);

            } else if (!isEnabled()) {
                symbolImageView.setColorFilter(DrawableUtil.getColorAlpha(getContext(), srcColor, getContext().getString(R.string.color_alpha_50)), PorterDuff.Mode.SRC_IN);

            } else if (!isPressed()) {
                symbolImageView.setColorFilter(srcColor, PorterDuff.Mode.SRC_IN);
            }
        }

        buttonTextView.setEnabled(isEnabled());
    }

    public void setText(CharSequence text) {
        buttonTextView.setText(text);
    }

    public void setTextColor(int textColor) {
        buttonTextView.setTextColor(textColor);
    }

    public void setTextColor(ColorStateList colors) {
        buttonTextView.setTextColor(colors);
    }

    public void setTextSize(int unit, float size) {
        buttonTextView.setTextSize(unit, size);
    }

    public void setTextSize(float sizeSP) {
        buttonTextView.setTextSize(sizeSP);
    }

    public void setSolidColor(@ColorInt int solidColor) {
        Drawable bgDrawable = getBackground();

        this.solidColor = solidColor;
        if (bgDrawable instanceof StateListDrawable) {
            setCurrentStateDrawable((StateListDrawable) bgDrawable);
        } else {
            StateListDrawable gradientDrawable = new StateListDrawable();
            GradientDrawable currentDrawable = new GradientDrawable();
            gradientDrawable.addState(new int[]{android.R.attr.state_enabled}, currentDrawable);
            setCurrentStateDrawable(gradientDrawable);
        }
    }

    public void setStrokeColor(int strokeColor) {
        Drawable bgDrawable = getBackground();

        this.strokeColor = strokeColor;
        if (bgDrawable instanceof StateListDrawable) {
            setCurrentStateDrawable((StateListDrawable) bgDrawable);
        } else {
            StateListDrawable gradientDrawable = new StateListDrawable();
            GradientDrawable currentDrawable = new GradientDrawable();
            gradientDrawable.addState(new int[]{android.R.attr.state_enabled}, currentDrawable);
            setCurrentStateDrawable(gradientDrawable);
        }
    }

    public void setStrokeWidth(int strokeWidthPx) {
        Drawable bgDrawable = getBackground();

        this.strokeWidthPx = strokeWidthPx;
        if (bgDrawable instanceof StateListDrawable) {
            setCurrentStateDrawable((StateListDrawable) bgDrawable);
        } else {
            StateListDrawable gradientDrawable = new StateListDrawable();
            GradientDrawable currentDrawable = new GradientDrawable();
            gradientDrawable.addState(new int[]{android.R.attr.state_enabled}, currentDrawable);
            setCurrentStateDrawable(gradientDrawable);
        }
    }

    public void setStroke(int strokeWidthPx, int strokeColor) {
        Drawable bgDrawable = getBackground();

        this.strokeWidthPx = strokeWidthPx;
        this.strokeColor = strokeColor;
        if (bgDrawable instanceof StateListDrawable) {
            setCurrentStateDrawable((StateListDrawable) bgDrawable);
        } else {
            StateListDrawable gradientDrawable = new StateListDrawable();
            GradientDrawable currentDrawable = new GradientDrawable();
            gradientDrawable.addState(new int[]{android.R.attr.state_enabled}, currentDrawable);
            setCurrentStateDrawable(gradientDrawable);
        }
    }

    private void setCurrentStateDrawable(StateListDrawable stateListDrawable) {
        GradientDrawable currentDrawable = (GradientDrawable) stateListDrawable.getCurrent();

        if (cornerRadius != null) {
            currentDrawable.setCornerRadii(cornerRadius);
        }

        currentDrawable.setColor(solidColor);
        currentDrawable.setStroke(strokeWidthPx, strokeColor);

        setBackgroundDrawable(stateListDrawable);
    }

    /**
     * 色に透明度を設定する
     *
     * @param parentColor
     * @param alpha
     * @return
     */
    private int getColorAlpha(Context context, int parentColor, String alpha) {
        String hexString = String.format("%02X", parentColor).substring(2);
        if (parentColor == ContextCompat.getColor(context, android.R.color.transparent)) {
            return parentColor;
        }

        if (parentColor == ContextCompat.getColor(context, android.R.color.black)) {
            hexString = "000000";

        }
        return Color.parseColor("#"
                + alpha
                + hexString);
    }

    public void setSrc(@DrawableRes int resId) {
        symbolImageView.setImageResource(resId);
    }

    public void setSrcColor(@ColorInt int color) {
        symbolImageView.setColorFilter(color);
    }

}