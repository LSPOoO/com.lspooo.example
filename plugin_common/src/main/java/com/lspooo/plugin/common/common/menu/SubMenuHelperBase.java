/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lspooo.plugin.common.common.menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.lspooo.plugin.common.R;
import com.lspooo.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.plugin.common.tools.DensityUtil;
import com.lspooo.plugin.common.tools.SystemBarTintManager;
import com.lspooo.plugin.common.ui.AbsActivity;
import com.lspooo.plugin.common.view.CCPListPopupWindow;

public abstract class SubMenuHelperBase implements View.OnKeyListener,
        ViewTreeObserver.OnGlobalLayoutListener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {

    /**上下文*/
    protected Context mContext;
    /**下拉菜单显示区域*/
    private CCPListPopupWindow mPopup;
    /**下拉菜单显示位置基准view*/
    private View mAnchorView;
    /**下拉菜单显示项目ViewGroup*/
    private ViewGroup mMeasureParent;
    /**下拉菜单数据适配器*/
    private BaseAdapter mBaseAdapter;
    SystemBarTintManager mSystemBarTintManager;
    private ViewTreeObserver mTreeObserver;

    /**
     * Callback when dismiss invoke in {@link CCPListPopupWindow}
     */
    private PopupWindow.OnDismissListener mOnDismissListener;

    /**
     * This will only be called when the dialog is canceled
     */
    private DialogInterface.OnCancelListener mOnCancelListener;

    private int mPopupMaxWidth;

    private int dividerHeight;
    /**状态栏的高度*/
    private int mStatusBarHeight  = 0;
    /**下拉菜单显示动画*/
    private int animationStyle = R.style.DropMenuAnimation;
    /** 当前是否竖屏显示*/
    private boolean isVertical = false;
    /** 是否没有状态栏 */
    private boolean noStatusBar = false;


    /**
     *
     */
    public SubMenuHelperBase(Context context) {
        mContext = context;
        Resources resources = context.getResources();
        mPopupMaxWidth = Math.max(4 * resources.getDisplayMetrics().widthPixels / 5,
                resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));

        if(mContext instanceof Activity) {
            ViewGroup viewGroup = (ViewGroup)((Activity)this.mContext).getWindow().getDecorView();
            if(viewGroup.getChildCount() <= 0) {
                mAnchorView = viewGroup;
            } else {
                mAnchorView = viewGroup.getChildAt(0);
            }
            mSystemBarTintManager = new SystemBarTintManager(((Activity)this.mContext));
        }

        dividerHeight = DensityUtil.getMetricsDensity(context, 1.0F);
        mBaseAdapter = buildMenuAdapter();
    }

    /**
     *
     */
    public final void enableStatusBar() {
        noStatusBar = false;
        animationStyle = R.style.DropMenuAnimation;
    }

    private int measureContentWidth(ListAdapter adapter) {
        // Menus don't tend to be long, so this is more sane than it looks.
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(mContext);
            }
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        return width;
    }

    public boolean tryShow() {

        if(mStatusBarHeight  == 0 && this.mContext instanceof Activity) {
            Rect rect = new Rect();
            // 状态栏的高度
            // decorView是window中的最顶层view，可以从window中获取到decorView，
            // 然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
            // 于是，我们就可以算出状态栏的高度了。
            ((Activity)this.mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            mStatusBarHeight = rect.top;
        }

        if(mStatusBarHeight <= 0 && mSystemBarTintManager != null) {
            mStatusBarHeight = mSystemBarTintManager.getConfig().getStatusBarHeight();
        }

        isVertical = isVerticalScreen();
        mPopup = new CCPListPopupWindow(mContext, null, R.attr.popupMenuStyle);
        mPopup.setOnDismissListener(this);
        mPopup.setOnItemClickListener(this);
        mPopup.setAdapter(mBaseAdapter);
        mPopup.setModalFocus();
        //mPopup.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ytx_ofm_menu_bg));
        mPopup.setAnimationStyle(animationStyle);
        mPopup.setHorizontalOffset(BackwardSupportUtil.fromDPToPix(mContext , 8));
        mPopup.resetLocation();

        View anchor = mAnchorView;
        if (anchor != null) {
            final boolean addGlobalListener = mTreeObserver == null;
            mTreeObserver = anchor.getViewTreeObserver(); // Refresh to latest
            if (addGlobalListener) {
                mTreeObserver.addOnGlobalLayoutListener(this);
            }
            mPopup.setAnchorView(anchor);
        } else {
            return false;
        }

        int height;
        if(mContext instanceof AbsActivity) {
            height = ((AbsActivity)this.mContext).getSupportActionBar().getHeight();
        } else {
            DisplayMetrics localDisplayMetrics = this.mContext.getResources().getDisplayMetrics();
            if (localDisplayMetrics.widthPixels <= localDisplayMetrics.heightPixels) {
                height = DensityUtil.getMetricsDensity(mContext, 49.0F);
            } else {
                height = DensityUtil.getMetricsDensity(mContext, 40.0F);
            }
        }
        mPopup.setVerticalOffset(height + mStatusBarHeight /*- mContext.getResources().getDimensionPixelSize(R.dimen.NormalPadding)*/);
        mPopup.setStatusBarVisiable(noStatusBar);
        mPopup.setContentWidth(Math.min(measureContentWidth(mBaseAdapter), mPopupMaxWidth));
        mPopup.setInputMethodModeNotNeeded();
        // setBackgroundAlpha(0.5f);
        mPopup.show();
        mPopup.getListView().setOnKeyListener(this);
        mPopup.getListView().setDivider(null);
        mPopup.getListView().setSelector(new ColorDrawable(this.mContext.getResources().getColor(android.R.color.transparent)));
        mPopup.getListView().setDividerHeight(0);
        mPopup.getListView().setVerticalScrollBarEnabled(false);
        mPopup.getListView().setHorizontalScrollBarEnabled(false);

        return true;
    }


    /**
     * @return 当前下拉菜单是否竖屏显示
     */
    private boolean isVerticalScreen() {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        return (displayMetrics.widthPixels <= displayMetrics.heightPixels);
    }

    /**
     * This method must implements by sub class.
     * @return 当前下拉菜单绑定的数据显示接口
     */
    protected abstract BaseAdapter buildMenuAdapter();

    /**
     *
     */
    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }


    @Override
    public void onDismiss() {
        mPopup = null;
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) {
                mTreeObserver = mAnchorView.getViewTreeObserver();
            }
            mTreeObserver.removeGlobalOnLayoutListener(this);
            mTreeObserver = null;
        }

        if(mOnCancelListener != null) {
            mOnCancelListener.onCancel(null);
        }

        if(mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        // setBackgroundAlpha(1.0f);
    }

    public boolean isShowing() {
        return mPopup != null && mPopup.isShowing();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        dismiss();
    }

    @Override
    public void onGlobalLayout() {
        if (isShowing()) {
            final View anchor = mAnchorView;
            if (anchor == null || !anchor.isShown()) {
                if(isVertical = isVerticalScreen()) {
                    return;
                }
                dismiss();
            } else if (isShowing()) {
                // Recompute window size and position
//                mPopup.show();
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && keyCode == KeyEvent.KEYCODE_MENU) {
            dismiss();
            return true;
        }
        return false;
    }

    /**
     * 设置下拉菜单返回键取消回调接口
     * @param onCancelListener 返回键取消回调接口
     */
    public final void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }

    /**
     * 设置下拉菜单关闭回调接口
     * @param onDismissListener 关闭回调接口
     */
    public final void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener){
        mOnDismissListener = onDismissListener;
    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        if(mContext != null && mContext instanceof Activity) {
           Window window = ((Activity)mContext).getWindow();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = bgAlpha;
            if (bgAlpha == 1) {
                // 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                // 此行代码主要是解决在华为手机上半透明效果无效的bug
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            window.setAttributes(lp);
        }

    }
}
