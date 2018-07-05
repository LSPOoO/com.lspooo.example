package com.lspooo.plugin.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspooo.plugin.camera.listener.CaptureListener;
import com.lspooo.plugin.camera.listener.ClickListener;
import com.lspooo.plugin.camera.listener.ReturnListener;
import com.lspooo.plugin.camera.listener.TypeListener;

public class CaptureLayout extends FrameLayout {

    private CaptureButton captureBtn;
    private TypeButton confirmBtn;
    private TypeButton cancelBtn;
    private ReturnButton returnBtn;
    private TextView tipTv;

    private CaptureListener captureListener;
    private TypeListener typeListener;
    private ReturnListener returnListener;      //退出按钮监听

    private int widgetWidth;
    private int widgetHeight;
    private int buttonSize;
    private boolean isFirst = true;

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidgetSize(context);
        initView();
    }

    private void initWidgetSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            widgetWidth = outMetrics.widthPixels;
        } else {
            widgetWidth = outMetrics.widthPixels / 2;
        }
        buttonSize = (int) (widgetWidth / 4.5f);
        widgetHeight = buttonSize + (buttonSize / 5) * 2 + 100;
    }

    private void initView() {
        setWillNotDraw(false);
        //拍照按钮
        captureBtn = new CaptureButton(getContext(), buttonSize);
        LayoutParams captureBtnParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        captureBtnParam.gravity = Gravity.CENTER;
        captureBtn.setLayoutParams(captureBtnParam);
        captureBtn.setCaptureListener(new CaptureListener() {
            @Override
            public void takePictures() {
                if (captureListener != null) {
                    captureListener.takePictures();
                }
            }

            @Override
            public void recordShort(long time) {
                if (captureListener != null) {
                    captureListener.recordShort(time);
                }
                startAlphaAnimation();
            }

            @Override
            public void recordStart() {
                if (captureListener != null) {
                    captureListener.recordStart();
                }
                startAlphaAnimation();
            }

            @Override
            public void recordEnd(long time) {
                if (captureListener != null) {
                    captureListener.recordEnd(time);
                }
                startAlphaAnimation();
                startTypeBtnAnimator();
            }

            @Override
            public void recordZoom(float zoom) {
                if (captureListener != null) {
                    captureListener.recordZoom(zoom);
                }
            }

            @Override
            public void recordError() {
                if (captureListener != null) {
                    captureListener.recordError();
                }
            }
        });

        //取消按钮
        cancelBtn = new TypeButton(getContext(), TypeButton.TYPE_CANCEL, buttonSize);
        final LayoutParams cancelBtnParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        cancelBtnParam.gravity = Gravity.CENTER_VERTICAL;
        cancelBtnParam.setMargins((widgetWidth / 4) - buttonSize / 2, 0, 0, 0);
        cancelBtn.setLayoutParams(cancelBtnParam);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeListener != null) {
                    typeListener.cancel();
                }
                startAlphaAnimation();
            }
        });

        //确认按钮
        confirmBtn = new TypeButton(getContext(), TypeButton.TYPE_CONFIRM, buttonSize);
        LayoutParams confirmBtnParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        confirmBtnParam.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        confirmBtnParam.setMargins(0, 0, (widgetWidth / 4) - buttonSize / 2, 0);
        confirmBtn.setLayoutParams(confirmBtnParam);
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeListener != null) {
                    typeListener.confirm();
                }
                startAlphaAnimation();
            }
        });

        //返回按钮
        returnBtn = new ReturnButton(getContext(), (int) (buttonSize / 2.5f));
        LayoutParams returnBtnParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        returnBtnParam.gravity = Gravity.CENTER_VERTICAL;
        returnBtnParam.setMargins(widgetWidth / 6, 0, 0, 0);
        returnBtn.setLayoutParams(returnBtnParam);
        returnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnListener != null) {
                    returnListener.onReturn();
                }
            }
        });

        tipTv = new TextView(getContext());
        LayoutParams txt_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txt_param.gravity = Gravity.CENTER_HORIZONTAL;
        txt_param.setMargins(0, 0, 0, 0);
        tipTv.setText("轻触拍照，长按摄像");
        tipTv.setTextColor(0xFFFFFFFF);
        tipTv.setGravity(Gravity.CENTER);
        tipTv.setLayoutParams(txt_param);

        this.addView(captureBtn);
        this.addView(cancelBtn);
        this.addView(confirmBtn);
        this.addView(returnBtn);
        this.addView(tipTv);

        cancelBtn.setVisibility(GONE);
        confirmBtn.setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widgetWidth, widgetHeight);
    }

    public void startTypeBtnAnimator() {
        //拍照录制结果后的动画
        returnBtn.setVisibility(GONE);
        captureBtn.setVisibility(GONE);
        cancelBtn.setVisibility(VISIBLE);
        confirmBtn.setVisibility(VISIBLE);
        cancelBtn.setClickable(false);
        confirmBtn.setClickable(false);
        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(cancelBtn, "translationX", widgetWidth / 4, 0);
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(confirmBtn, "translationX", -widgetWidth / 4, 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator_cancel, animator_confirm);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cancelBtn.setClickable(true);
                confirmBtn.setClickable(true);
            }
        });
        set.setDuration(200);
        set.start();
    }

    /**************************************************
     * 对外提供的API                      *
     **************************************************/
    public void resetCaptureLayout() {
        captureBtn.resetState();
        cancelBtn.setVisibility(GONE);
        confirmBtn.setVisibility(GONE);
        returnBtn.setVisibility(VISIBLE);
    }


    public void startAlphaAnimation() {
        if (isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(tipTv, "alpha", 1f, 0f);
            animator_txt_tip.setDuration(500);
            animator_txt_tip.start();
            isFirst = false;
        }
    }

    public void setTextWithAnimation(String tip) {
        tipTv.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(tipTv, "alpha", 0f, 1f, 1f, 0f);
        animator_txt_tip.setDuration(2500);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        captureBtn.setDuration(duration);
    }

    public void setButtonFeatures(int state) {
        captureBtn.setButtonFeatures(state);
    }

    public void setTip(String tip) {
        tipTv.setText(tip);
    }

    public void showTip() {
        tipTv.setVisibility(VISIBLE);
    }


    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }

    public void setReturnLisenter(ReturnListener returnListener) {
        this.returnListener = returnListener;
    }
}
