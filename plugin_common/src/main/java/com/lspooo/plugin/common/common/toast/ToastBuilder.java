package com.lspooo.plugin.common.common.toast;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.lspooo.plugin.common.LSPApplicationContext;
import com.lspooo.plugin.common.R;

public class ToastBuilder {
   private ToastBuilder() {
   }

   static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor) {
       drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
       return drawable;
   }

   static Drawable tint9PatchDrawableFrame(@ColorInt int tintColor) {
       final Drawable toastDrawable = getDrawable(R.drawable.toast_frame);
       return tintIcon(toastDrawable, tintColor);
   }

   static void setBackground(@NonNull View view, Drawable drawable) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
           view.setBackground(drawable);
       else
           view.setBackgroundDrawable(drawable);
   }

   public static Drawable getDrawable(@DrawableRes int id) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
           return LSPApplicationContext.getContext().getDrawable(id);
       else
           return LSPApplicationContext.getContext().getResources().getDrawable(id);
   }
}
