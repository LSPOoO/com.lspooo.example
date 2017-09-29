package com.lspooo.plugin.common.tools;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.lspooo.plugin.common.R;

public class AnimatorUtils {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void updateViewAnimation(View arcView , long duration , float translationX , final OnAnimationListener listener) {
        if(arcView == null || SDKVersionUtils.isSmallerorEqual(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
            return ;
        }
        Animator animator = (Animator) arcView.getTag(R.anim.property_anim);
        if (animator != null) {
            animator.cancel();
        }
        arcView.animate().cancel();
        if (listener == null) {
            arcView.animate().setDuration(duration).translationX(translationX).translationY(0.0F);
            return;
        }
        arcView.animate().setDuration(duration).translationX(translationX).translationY(0.0F).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void startViewAnimation(View arcView , float translationX) {
        if(arcView == null || SDKVersionUtils.isSmallerorEqual(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
            return ;
        }

        Animator animator = (Animator) arcView.getTag(R.anim.property_anim);
        if(animator != null) {
            animator.cancel();
        }
        arcView.animate().cancel();
        arcView.setTranslationX(translationX);
        arcView.setTranslationY(0.0F);
    }

    public static void hideView(final View hideView){
        hideView.startAnimation(AnimatorUtils.moveToViewBottom(200,
                new OnAnimationListener() {
                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        hideView.setVisibility(View.GONE);
                    }
                }));
    }

    public static void showView(final View showView){
        showView.startAnimation(AnimatorUtils.moveToViewLocation(200,
                new OnAnimationListener() {
                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        showView.setVisibility(View.VISIBLE);
                    }
                }));
    }
    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return 动画
     */
    private static TranslateAnimation moveToViewLocation(long duration, final OnAnimationListener listener) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(duration);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null){
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return mHiddenAction;
    }



    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return 动画
     */
    private static TranslateAnimation moveToViewBottom(long duration, final OnAnimationListener listener) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(duration);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener != null){
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return mHiddenAction;
    }

    public static void switchTwoView(final View oldView, final View newView, final long duration){
        oldView.startAnimation(AnimatorUtils.moveToViewBottom(150,
                new OnAnimationListener() {
                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        oldView.setVisibility(View.GONE);
                        newView.setVisibility(View.VISIBLE);
                        newView.setAnimation(AnimatorUtils.moveToViewLocation(duration, null));
                    }
                }));
    }

    public interface OnAnimationListener {

        void onAnimationCancel();
        void onAnimationEnd();
    }


}
