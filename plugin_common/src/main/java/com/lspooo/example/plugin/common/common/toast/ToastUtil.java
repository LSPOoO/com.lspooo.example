package com.lspooo.example.plugin.common.common.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lspooo.example.plugin.common.LSPApplicationContext;
import com.lspooo.example.plugin.common.R;

@SuppressLint("InflateParams")
public class ToastUtil {
    @ColorInt
    private static int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    @ColorInt
    private static int ERROR_COLOR = Color.parseColor("#353A3E");
    @ColorInt
    private static int INFO_COLOR = Color.parseColor("#3F51B5");
    @ColorInt
    private static int SUCCESS_COLOR = Color.parseColor("#388E3C");
    @ColorInt
    private static int WARNING_COLOR = Color.parseColor("#FFA900");
    @ColorInt
    private static int NORMAL_COLOR = Color.parseColor("#353A3E");

    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    private static int textSize = 16; // in SP

    private static boolean tintIcon = true;

    private ToastUtil() {
        // avoiding instantiation
    }

    public static void normal(@NonNull CharSequence message) {
         normal( message, Toast.LENGTH_SHORT, null, false);
    }

    public static void normal( @NonNull CharSequence message, Drawable icon) {
         normal( message, Toast.LENGTH_SHORT, icon, true);
    }

    public static void normal( @NonNull CharSequence message, int duration) {
         normal( message, duration, null, false);
    }

    public static void normal(@NonNull CharSequence message, int duration,
                               Drawable icon) {
        normal( message, duration, icon, true);
    }

    public static void normal( @NonNull CharSequence message, int duration,
                               Drawable icon, boolean withIcon) {
        custom(message, icon, NORMAL_COLOR, duration, withIcon, true);
    }

    public static void warning( @NonNull CharSequence message) {
         warning(message, Toast.LENGTH_SHORT, true);
    }

    public static void warning( @NonNull CharSequence message, int duration) {
         warning(message, duration, true);
    }

    public static void warning( @NonNull CharSequence message, int duration, boolean withIcon) {
         custom( message, ToastBuilder.getDrawable( R.drawable.ic_toast_warn),
                WARNING_COLOR, duration, withIcon, true);
    }

    public static void info( @NonNull CharSequence message) {
         info( message, Toast.LENGTH_SHORT, true);
    }

    public static void info( @NonNull CharSequence message, int duration) {
         info( message, duration, true);
    }

    public static void info( @NonNull CharSequence message, int duration, boolean withIcon) {
        custom( message, ToastBuilder.getDrawable( R.drawable.ic_toast_error),
                INFO_COLOR, duration, withIcon, true);
    }

    public static void success(@NonNull CharSequence message) {
        success( message, Toast.LENGTH_SHORT, true);
    }

    public static void success( @NonNull CharSequence message, int duration) {
         success( message, duration, true);
    }

    public static void success( @NonNull CharSequence message, int duration, boolean withIcon) {
        custom( message, ToastBuilder.getDrawable( R.drawable.ic_toast_success),
               SUCCESS_COLOR, duration, withIcon, true);
    }

    public static void error( @NonNull CharSequence message) {
        error( message, Toast.LENGTH_SHORT, true);
    }

    public static void error( @NonNull CharSequence message, int duration) {
        error( message, duration, true);
    }

    public static void error(@NonNull CharSequence message, int duration, boolean withIcon) {
         custom( message, ToastBuilder.getDrawable( R.drawable.ic_toast_error),
                ERROR_COLOR, duration, withIcon, true);
    }

    public static void custom( @NonNull CharSequence message, Drawable icon,
                               int duration, boolean withIcon) {
        custom( message, icon, -1, duration, withIcon, false);
    }

    public static void custom( @NonNull CharSequence message, @DrawableRes int iconRes,
                               @ColorInt int tintColor, int duration,
                               boolean withIcon, boolean shouldTint) {
        custom( message, ToastBuilder.getDrawable( iconRes),
                tintColor, duration, withIcon, shouldTint);
    }

    public static void custom( @NonNull CharSequence message, Drawable icon,
                               @ColorInt int tintColor, int duration,
                               boolean withIcon, boolean shouldTint) {
        final Toast currentToast = new Toast(LSPApplicationContext.getContext());
        final View toastLayout = ((LayoutInflater) LSPApplicationContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_common_toast, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        currentToast.setGravity(Gravity.CENTER,0,0);
        if (shouldTint)
            drawableFrame = ToastBuilder.tint9PatchDrawableFrame(tintColor);
        else
            drawableFrame = ToastBuilder.getDrawable(R.drawable.toast_frame);
        ToastBuilder.setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            if (tintIcon)
                icon = ToastBuilder.tintIcon(icon, DEFAULT_TEXT_COLOR);
            ToastBuilder.setBackground(toastIcon, icon);
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        toastTextView.setTextColor(DEFAULT_TEXT_COLOR);
        toastTextView.setText(message);
        toastTextView.setTypeface(currentTypeface);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        currentToast.show();
    }

    public static class Config {
        @ColorInt
        private int DEFAULT_TEXT_COLOR = ToastUtil.DEFAULT_TEXT_COLOR;
        @ColorInt
        private int ERROR_COLOR = ToastUtil.ERROR_COLOR;
        @ColorInt
        private int INFO_COLOR = ToastUtil.INFO_COLOR;
        @ColorInt
        private int SUCCESS_COLOR = ToastUtil.SUCCESS_COLOR;
        @ColorInt
        private int WARNING_COLOR = ToastUtil.WARNING_COLOR;

        private Typeface typeface = ToastUtil.currentTypeface;
        private int textSize = ToastUtil.textSize;

        private boolean tintIcon = ToastUtil.tintIcon;

        private Config() {
            // avoiding instantiation
        }

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        public static void reset() {
            ToastUtil.DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
            ToastUtil.ERROR_COLOR = Color.parseColor("#D50000");
            ToastUtil.INFO_COLOR = Color.parseColor("#3F51B5");
            ToastUtil.SUCCESS_COLOR = Color.parseColor("#388E3C");
            ToastUtil.WARNING_COLOR = Color.parseColor("#FFA900");
            ToastUtil.currentTypeface = LOADED_TOAST_TYPEFACE;
            ToastUtil.textSize = 16;
            ToastUtil.tintIcon = true;
        }

        @CheckResult
        public Config setTextColor(@ColorInt int textColor) {
            DEFAULT_TEXT_COLOR = textColor;
            return this;
        }

        @CheckResult
        public Config setErrorColor(@ColorInt int errorColor) {
            ERROR_COLOR = errorColor;
            return this;
        }

        @CheckResult
        public Config setInfoColor(@ColorInt int infoColor) {
            INFO_COLOR = infoColor;
            return this;
        }

        @CheckResult
        public Config setSuccessColor(@ColorInt int successColor) {
            SUCCESS_COLOR = successColor;
            return this;
        }

        @CheckResult
        public Config setWarningColor(@ColorInt int warningColor) {
            WARNING_COLOR = warningColor;
            return this;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        public void apply() {
            ToastUtil.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR;
            ToastUtil.ERROR_COLOR = ERROR_COLOR;
            ToastUtil.INFO_COLOR = INFO_COLOR;
            ToastUtil.SUCCESS_COLOR = SUCCESS_COLOR;
            ToastUtil.WARNING_COLOR = WARNING_COLOR;
            ToastUtil.currentTypeface = typeface;
            ToastUtil.textSize = textSize;
            ToastUtil.tintIcon = tintIcon;
        }
    }
}
