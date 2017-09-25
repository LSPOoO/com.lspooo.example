package com.lspooo.example.plugin.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lspooo.example.plugin.common.R;
import com.lspooo.example.plugin.common.tools.AnimatorUtils;
import com.lspooo.example.plugin.common.tools.ECHandlerHelper;
import com.lspooo.example.plugin.common.common.swipe.MethodInvoke;
import com.lspooo.example.plugin.common.tools.SDKVersionUtils;
import com.lspooo.example.plugin.common.common.swipe.SwipeActivityManager;
import com.lspooo.example.plugin.common.common.swipe.SwipeTranslucentMethodUtils;
import com.lspooo.example.plugin.common.common.swipe.ViewDragHelper;

import java.lang.ref.WeakReference;


/**
 * com.yuntongxun.rongxin.common.view in ECDemo_Android
 * Created by Jorstin on 2015/6/6.
 */
public class SwipeBackLayout extends FrameLayout {

    private static final String TAG = "RongXin.SwipeBackLayout";
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 100; // dips per second
    private static final int FULL_ALPHA = 255;
    private boolean mInLayout;
    private boolean mEnable = true;
    public View mContentView;
    private float mScrimOpacity;
    private ViewDragHelper mDragHelper;
    private float mScrollPercent;
    private Drawable mShadowLeft;
    /**
     * Threshold of scroll, we will close the activity, when scrollPercent over
     * this value;
     */
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
    private Rect mTmpRect = new Rect();
    /**
     * Default threshold of scroll
     */
    private static final float DEFAULT_SCROLL_THRESHOLD = 1.0f;
    private static final int OVERSCROLL_DISTANCE = 10;
    private int mContentLeft;
    private int mContentTop;
    public boolean mScrolling = false;
    public boolean mTranslucent = false;
    private boolean mRequestTranslucent = false;
    private boolean mFastRelease = false;
    public OnSwipeGestureDelegate mOnSwipeGestureDelegate;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mShadowLeft = getResources().getDrawable(R.drawable.shadow_left);
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        init();
    }

    public boolean isSwipeBacking() {
        isScrolling();
        return mScrolling;
    }

    public boolean isScrolling() {
        if(!mScrolling) {
            return false;
        }
        if(Float.compare(this.mContentView.getLeft(), 0.01F) <= 0) {
            mScrolling = false;
            return false;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        try {
            boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(event);
            return shouldIntercept;
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: handle exception
            // issues #9
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handler = super.dispatchTouchEvent(ev);
        return handler;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;
        boolean ret = false;
        try {
            ret = super.drawChild(canvas, child, drawingTime);
            if (Float.compare(mScrimOpacity , 0.0F) > 0 && drawContent
                    && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
                drawShadow(canvas, child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);
        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
        mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
        mShadowLeft.draw(canvas);
    }


    public void init() {
        mScrollThreshold = 0.3f;
        mDragHelper = ViewDragHelper.create(this , new ViewDragCallback());
        setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setMaxVelocity(minVel * 3f);
        mContentLeft = 0;
        mContentTop = 0;
    }

    public void onFinishInflate() {
        mContentView = this;
        super.onFinishInflate();
    }

    public void markTranslucent(boolean translucent) {
        mTranslucent = translucent;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        if (mContentView != null)
            mContentView.layout(mContentLeft, mContentTop,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentTop + mContentView.getMeasuredHeight());
        mInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    /**
     * Set up contentView which will be moved by user gesture
     *
     * @param view
     */
    public void setContentView(View view) {
        mContentView = view;
    }

    public void setEnableGesture(boolean enable) {
        mEnable = enable;
    }

    public void setNeedRequestActivityTranslucent( boolean request) {
        mRequestTranslucent = request;
    }

    public void setSwipeGestureDelegate(OnSwipeGestureDelegate onSwipeGestureDelegate) {
        mOnSwipeGestureDelegate = onSwipeGestureDelegate;
    }

    /**
     * Enable edge tracking for the selected edges of the parent view. The
     * callback's
     * {@link ViewDragHelper.Callback#onEdgeTouched(int, int)}
     * and
     * methods will only be invoked for edges for which edge tracking has been
     * enabled.
     *
     * @param edgeFlags Combination of edge flags describing the edges to watch
     */
    public void setEdgeTrackingEnabled(int edgeFlags) {
        mDragHelper.setEdgeTrackingEnabled(edgeFlags);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mScrimOpacity = Math.max(0.0F, 1.0F - mScrollPercent);
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback implements MethodInvoke.OnSwipeInvokeResultListener {
        private static final String TAG = "RongXin.ViewDragCallback";
        private int mTemp = 0;
        private int mReleasedLeft = 0;
        private boolean mIsScrollOverValid;

        // 判断child是否是拖动的目标
        @Override
        public boolean tryCaptureView(View view, int i) {
            boolean edgeTouched = mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, i);
            return edgeTouched;
        }

        //  拖动手势释放后的处理
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            final int childWidth = releasedChild.getWidth();
            int left = ((xvel > 0.0F) || (xvel == 0.0F) && (mScrollPercent > mScrollThreshold)) ? childWidth + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE : 0;
            int top = 0;
            mReleasedLeft = left;
            //mIsScrollOverValid = true;
            if(!mTranslucent) {
                if(SwipeBackLayout.this.getContext() instanceof Activity) {
                    Activity mActivity = ((Activity)SwipeBackLayout.this.getContext());
                    if (left > 0 && !mActivity.isFinishing()) {
                        mActivity.finish();
                        mActivity.overridePendingTransition(0, R.anim.slide_right_out);
                    }
                }
                return ;
            }
            // settleCapturedViewAt中调用了ViewDragHelper内部mScroller的startScroll()方法，然后通过invalidate刷新就可以触发SwipeBackLayout的自行滚动
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
            mFastRelease = true;
        }

        // 拖动状态的改变
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            Activity mActivity = null;
            if(state == ViewDragHelper.STATE_DRAGGING) {
                if(SwipeBackLayout.this.getContext() instanceof Activity) {
                    ((Activity)SwipeBackLayout.this.getContext()).getWindow().getDecorView().setBackgroundResource(R.color.transparent);
                }
                if(mOnSwipeGestureDelegate != null) {
                    mOnSwipeGestureDelegate.onDragging();
                }
                mIsScrollOverValid = false;
                if(mTranslucent) {
                    SwipeActivityManager.notifySwipe(0.0F);
                }
            }

            if(state == ViewDragHelper.STATE_IDLE && !mFastRelease) {
                if(mOnSwipeGestureDelegate != null) {
                    mOnSwipeGestureDelegate.onCancel();
                }
                SwipeActivityManager.notifySwipe(1.0F);
            }

            if(state == ViewDragHelper.STATE_DRAGGING /*&& mScrolling*/ && (SwipeBackLayout.this.getContext() instanceof Activity) && !mTranslucent && !mRequestTranslucent) {
                mActivity = ((Activity)SwipeBackLayout.this.getContext());
                if(SDKVersionUtils.isGreatThanOrEqualTo(16)) {
                }
            }
            if(state == ViewDragHelper.STATE_SETTLING) {
                boolean open = (mReleasedLeft > 0);
                SwipeActivityManager.notifySettle(open, mReleasedLeft);
            }
            if(mActivity != null) {
                MethodInvoke.SwipeInvocationHandler handler = new MethodInvoke.SwipeInvocationHandler();
                handler.mWeakReference = new WeakReference<MethodInvoke.OnSwipeInvokeResultListener>(this);
                SwipeTranslucentMethodUtils.convertActivityToTranslucent(mActivity , handler);
            }
        }

        // 拖动后view位置的改变
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(!mTranslucent) {
                return ;
            }
            mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            mContentLeft = left;
            mContentTop = top;
            invalidate();
            if(Float.compare(mScrollPercent , DEFAULT_SCROLL_THRESHOLD) >=0 && !mIsScrollOverValid) {
                mIsScrollOverValid = true;
                mScrolling = true;
                ECHandlerHelper.postRunnOnUI(new Runnable() {
                    @Override
                    public void run() {
                        if(mOnSwipeGestureDelegate != null) {
                            mOnSwipeGestureDelegate.onSwipeBack();
                        }
                        mTranslucent = false;
                    }
                });
            } else {
                if(!(Float.compare(mScrollPercent, 0.01F) > 0)) {
                    mIsScrollOverValid = false;
                    mScrolling = false;
                }
            }
            if(mDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING) {
                SwipeActivityManager.notifySwipe(mScrollPercent);
            }
        }

        // 拖动位置的处理，可以处理拖动过程中的最高位置或者最低位置
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if (mTranslucent) {
                int max = Math.max(mTemp, left);
                mTemp = 0;
                ret = Math.min(child.getWidth(), Math.max(max, 0));
            } else {
                mTemp = Math.max(mTemp, left);
            }
            return ret;
        }


        @Override
        public int getViewHorizontalDragRange(View child) {
            return ViewDragHelper.EDGE_LEFT;
        }

        @Override
        public void onSwipeInvoke(final boolean result) {
            ECHandlerHelper.postRunnOnUI(new Runnable() {
                @Override
                public void run() {
                    SwipeBackLayout.this.markTranslucent(result);
                    if(result) {
                        if(mReleasedLeft > 0) {
                            SwipeActivityManager.notifySwipe(0.0F);
                            if(!mFastRelease) {
                                if(mReleasedLeft == 0) {
                                    AnimatorUtils.updateViewAnimation(SwipeBackLayout.this, 200L, 0.0F, new AnimatorUtils.OnAnimationListener() {
                                        @Override
                                        public void onAnimationCancel() {
                                            onAnimationEnd();
                                        }

                                        @Override
                                        public void onAnimationEnd() {
                                            mTranslucent = false;
                                        }
                                    });
                                } else {
                                    AnimatorUtils.updateViewAnimation(SwipeBackLayout.this, 200L, mReleasedLeft, new AnimatorUtils.OnAnimationListener() {
                                        @Override
                                        public void onAnimationCancel() {
                                            onAnimationEnd();
                                        }

                                        @Override
                                        public void onAnimationEnd() {
                                            mIsScrollOverValid = true;
                                            ECHandlerHelper.postRunnOnUI(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(mOnSwipeGestureDelegate != null) {
                                                        mOnSwipeGestureDelegate.onSwipeBack();
                                                    }
                                                    SwipeActivityManager.notifySwipe(1.0F);
                                                    mIsScrollOverValid = false;
                                                    mScrolling = false;
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }
    }



    public interface OnSwipeGestureDelegate {
        void onSwipeBack();
        void onDragging();
        void onCancel();
        void onDealDecorView();
    }
}
