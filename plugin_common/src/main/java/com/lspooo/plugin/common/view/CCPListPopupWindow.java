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
package com.lspooo.plugin.common.view;


import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lspooo.plugin.common.R;

/**
 * 一个展示列表的浮动窗口
 * @author 容联•云通讯
 * @since 2017/3/16
 */
public class CCPListPopupWindow {
	
	public static final String TAG = "RongXin.CCPListPopupWindow";

    /**
     * This value controls the length of time that the user must leave a pointer down without
     * scrolling to expand the autocomplete dropdown list to cover the IME.
     */
    private static final int EXPAND_LIST_TIMEOUT = 250;
    
	/**
	 * 
	 */
	private Context mContext;
	
	/**
	 * 
	 */
	private Rect mTempRect ;
	
	/**
	 * 
	 */
	private Handler mHandler;
	
	/**
	 * ListView selector.
	 */
	private Drawable mDropDownListHighlight;
	
	/**
	 * The Callbacks for {@link #mHandler}
	 */
	private ResizePopupRunnable mResizePopupRunnable;
	
	/**
	 * The Callbacks for {@link CCPPopupListView}
	 */
	private OnListScrollListener mScrollListener;
	
	private PopupWindow mPopupWindow;
	
	/**
	 * The ListView for {@link CCPListPopupWindow}
	 */
	private CCPDropDownListView mDropDownList;
	
	/**
	 * The adapter in ListView for {@link CCPListPopupWindow}
	 */
	private ListAdapter mListAdapter;
	
	/**
	 * 
	 */
	private DataSetObserver mDataSetObserver;
	
	/**
	 * event callback int ListView
	 */
	private AdapterView.OnItemClickListener mItemClickListener;
	
	/**
	 * event callback int ListView
	 */
	private AdapterView.OnItemSelectedListener mOnItemSelectedListener;
	
	private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
    private final ListSelectorHider mHideSelector = new ListSelectorHider();
	
	private Runnable mShowDropDownRunnable;
	
	/**
	 * @see PopupWindow#showAtLocation(View, int, int, int);
	 */
	private View mDropDownAnchorView;
	
	private View mPromptView;
    private int mPromptPosition = POSITION_PROMPT_ABOVE;
	
    private int mDropDownHorizontalOffset;
	
	/**
	 * 
	 */
	private boolean mDropDownVerticalOffsetSet;
	
	/**
	 * if the {@link CCPListPopupWindow} is focusable
	 */
	private boolean mModal;
	
	/**
	 * 
	 */
	private boolean mStatusBarVisiable = false;
	
    private boolean mDropDownAlwaysVisible = false;
    private boolean mForceIgnoreOutsideTouch = false;
	
	/**
	 * 
	 */
	private int mDropDownVerticalOffset;
	
    private int mDropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mDropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	/**
	 * the x location offset in {@link CCPListPopupWindow#}
	 */
	private int xLocation;
	
    /**
     * The provided prompt view should appear above list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_ABOVE = 0;

    /**
     * The provided prompt view should appear below list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_BELOW = 1;
	
	
	public CCPListPopupWindow(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.listPopupWindowStyle);
	}
	
	/**
	 * 
	 */
	public CCPListPopupWindow(Context context, AttributeSet attrs, int defStyle) {
		mContext = context;
		
		mDropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
		mDropDownVerticalOffset = Integer.MAX_VALUE;
		
		mHandler = new Handler();
		mTempRect = new Rect();
		mPopupWindow = new PopupWindow(context, attrs, defStyle);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		
		mResizePopupRunnable = new ResizePopupRunnable(this);
		mScrollListener = new OnListScrollListener(this);
	}
	
	public final void resetLocation(){
		xLocation = 0;
	}
	
	/**
	 * 
	 */
	public final void clearListSelection() {
		CCPDropDownListView listView = mDropDownList;
		if(listView == null) {
			return;
		}
		listView.requestLayout();
	}
	
	/**
	 * dismiss the {@link CCPListPopupWindow}
	 */
	public final void dismiss() {
		mPopupWindow.dismiss();
		removePromptView();
		mPopupWindow.setContentView(null);
		mDropDownList = null;
		mHandler.removeCallbacks(mResizePopupRunnable);
	}

	public final View getContentView() {
		return mPopupWindow.getContentView();
	}
	
    private void removePromptView() {
        if (mPromptView != null) {
            final ViewParent parent = mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                final ViewGroup group = (ViewGroup) parent;
                group.removeView(mPromptView);
            }
        }
    }
    
    /**
     * Forces outside touches to be ignored. Normally if {@link #isDropDownAlwaysVisible()} is
     * false, we allow outside touch to dismiss the dropdown. If this is set to true, then we ignore
     * outside touch even when the drop down is not set to always visible.
     *
     * @hide Used only by AutoCompleteTextView to handle some internal special cases.
     */
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    /**
     * Sets whether the drop-down should remain visible under certain conditions.
     *
     * The drop-down will occupy the entire screen below {@link #getAnchorView} regardless of the
     * size or content of the list.  {@link #getBackground()} will fill any space that is not used
     * by the list.
     *
     * @param dropDownAlwaysVisible Whether to keep the drop-down visible.
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    /**
     * @return Whether the drop-down is visible under special conditions.
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public boolean isDropDownAlwaysVisible() {
        return mDropDownAlwaysVisible;
    }
	
	 /**
     * Returns whether the popup window will be modal when shown.
     *
     * @return {@code true} if the {@link CCPListPopupWindow} will be modal, {@code false} otherwise.
     */
    public boolean isModal() {
        return mModal;
    }

	
	/**
	 * Changes the focusability of the {@link CCPListPopupWindow}
	 */
	public final void setModalFocus() {
		setModal(true);
	}
	
	/**
	 * 
	 * @param isVisiable
	 */
	public final void setStatusBarVisiable(boolean isVisiable) {
		mStatusBarVisiable = isVisiable;
	}
	
    /**
     * Set where the optional prompt view should appear. The default is {@link
     * #POSITION_PROMPT_ABOVE}.
     *
     * @param position A position constant declaring where the prompt should be displayed.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public void setPromptPosition(int position) {
        mPromptPosition = position;
    }

    /**
     * @return Where the optional prompt view should appear.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public int getPromptPosition() {
        return mPromptPosition;
    }
    
	
	/**
     * Set whether this window should be modal when shown.
     *
     * <p>If a popup window is modal, it will receive all touch and key input. If the user touches
     * outside the popup window's content area the popup window will be dismissed.
     *
     * @param modal {@code true} if the popup window should be modal, {@code false} otherwise.
     */
    public void setModal(boolean modal) {
        mModal = true;
        mPopupWindow.setFocusable(modal);
    }
    
	
	/**
	 * Control how the {@link CCPListPopupWindow} operates with an input method that {@link PopupWindow#INPUT_METHOD_NOT_NEEDED}
	 */
	public final void setInputMethodModeNotNeeded() {
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
	}
	
    /**
     * @return The width of the popup window in pixels.
     */
    public int getWidth() {
        return mDropDownWidth;
    }

    /**
     * Sets the width of the popup window in pixels. Can also be  or .
     *
     * @param width Width of the popup window.
     */
    public void setWidth(int width) {
        mDropDownWidth = width;
    }
    
	/**
	 * The Content width in {@link CCPListPopupWindow}
	 * @param width
	 */
	public final void setContentWidth(int width) {
		Drawable background = mPopupWindow.getBackground();
		if(background != null) {
			background.getPadding(mTempRect);
			mDropDownWidth = width + mTempRect.left + mTempRect.right;
			return;
		}
		setWidth(width);
	}
	
    /**
     * @return The height of the popup window in pixels.
     */
    public int getHeight() {
        return mDropDownHeight;
    }

    /**
     * Sets the height of the popup window in pixels. Can also be .
     *
     * @param height Height of the popup window.
     */
    public void setHeight(int height) {
        mDropDownHeight = height;
    }
	
	/**
	 * The ListView in {@link CCPListPopupWindow}
	 * @return
	 */
	public final ListView getListView() {
		return mDropDownList;
	}
	
	/**
     * Sets the operating mode for the soft input area.
     *
     * @param mode The desired mode, see {@link android.view.WindowManager.LayoutParams#softInputMode}
     *             for the full list
     * @see android.view.WindowManager.LayoutParams#softInputMode
     * @see #getSoftInputMode()
     */
    public void setSoftInputMode(int mode) {
    	mPopupWindow.setSoftInputMode(mode);
    }

    /**
     * Returns the current value in {@link #setSoftInputMode(int)}.
     *
     * @see #setSoftInputMode(int)
     * @see android.view.WindowManager.LayoutParams#softInputMode
     */
    public int getSoftInputMode() {
        return mPopupWindow.getSoftInputMode();
    }
    
	/**
	 * the current value isn't
	 * @return
	 */
	public final boolean isInputMethodNotNeeded() {
		return mPopupWindow.getInputMethodMode() != PopupWindow.INPUT_METHOD_NOT_NEEDED;
	}
	
	/**
     * <p>Indicate whether this {@link CCPListPopupWindow} window is showing on screen.</p>
     *
     * @return true if the {@link CCPListPopupWindow} is showing, false otherwise
     */
	public final boolean isShowing() {
		return mPopupWindow.isShowing();
	}
	
	/**
     * Sets the adapter that provides the data and the views to represent the data in this
     * {@link CCPListPopupWindow}.
     *
     * @param adapter The adapter to use to create this window's content.
     */
	public final void setAdapter(ListAdapter adapter) {
		if(mDataSetObserver == null) {
			mDataSetObserver = new PopupDataSetObserver();
		} else if(mListAdapter != null) {
			mListAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		
		mListAdapter = adapter;
		if(mListAdapter != null) {
			adapter.registerDataSetObserver(mDataSetObserver);
		}
		
		if(mDropDownList != null) {
			mDropDownList.setAdapter(mListAdapter);
			
		}
		
	}
	
	 /**
     * Returns the view that will be used to anchor this popup.
     *
     * @return The {@link CCPListPopupWindow} anchor view
     */
	public final View getAnchorView() {
		return mDropDownAnchorView;
	}
	
    /**
     * Sets the {@link CCPListPopupWindow} 's anchor view. This {@link CCPListPopupWindow} will always be positioned relative to the anchor
     * view when shown.
     *
     * @param parent The view to use as an anchor.
     */
	public final void setAnchorView(View parent) {
		mDropDownAnchorView = parent;
	}
	
	/**
     * Set an animation style to use when the popup window is shown or dismissed.
     *
     * @param animationStyle Animation style to use.
     */
    public void setAnimationStyle(int animationStyle) {
    	mPopupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * Returns the animation style that will be used when the popup window is shown or dismissed.
     *
     * @return Animation style that will be used.
     */
    public int getAnimationStyle() {
        return mPopupWindow.getAnimationStyle();
    }
    
    /**
     * Sets a drawable to use as the list item selector.
     *
     * @param selector List selector drawable to use in the popup.
     */
    public void setListSelector(Drawable selector) {
        mDropDownListHighlight = selector;
    }
	
	/**
     * @return The background drawable for the popup window.
     */
    public Drawable getBackground() {
        return mPopupWindow.getBackground();
    }
	
	/**
     * Sets a drawable to be the background for the {@link CCPListPopupWindow}
     *
     * @param background A drawable to set as the background.
     */
	public final void setBackgroundDrawable(Drawable background) {
		mPopupWindow.setBackgroundDrawable(background);
	}
	
    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param onDismissListener Listener that will be notified when the popup is dismissed.
     */
	public final void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
		mPopupWindow.setOnDismissListener(onDismissListener);
	}
	
    /**
     * Sets a listener to receive events when a list item is clicked.
     *
     * @param l Listener to register
     * @see ListView#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
	public final void setOnItemClickListener(AdapterView.OnItemClickListener l) {
		mItemClickListener = l;
	}
	
    /**
     * Sets a listener to receive events when a list item is selected.
     *
     * @param selectedListener Listener to register.
     * @see ListView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
     */
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
    	mOnItemSelectedListener = selectedListener;
    }
	
    /**
     * @return The horizontal offset of the {@link CCPListPopupWindow} from its anchor in pixels.
     */
    public int getHorizontalOffset() {
        return mDropDownHorizontalOffset;
    }

    /**
     * Set the horizontal offset of this {@link CCPListPopupWindow} from its anchor view in pixels.
     *
     * @param offset The horizontal offset of the popup from its anchor.
     */
    public void setHorizontalOffset(int offset) {
        mDropDownHorizontalOffset = offset;
    }
	
    /**
     * @return The vertical offset of the popup from its anchor in pixels.
     */
    public int getVerticalOffset() {
        if (!mDropDownVerticalOffsetSet) {
            return 0;
        }
        return mDropDownVerticalOffset;
    }
	
    /**
     * Set the vertical offset of this popup from its anchor view in pixels.
     *
     * @param verticalOffset The vertical offset of the popup from its anchor.
     */
	public final void setVerticalOffset(int verticalOffset){
		mDropDownVerticalOffset = verticalOffset;
		mDropDownVerticalOffsetSet = true;
	}
	
    /**
     * Set a view to act as a user prompt for this popup window. Where the prompt view will appear
     * is controlled by {@link #setPromptPosition(int)}.
     *
     * @param prompt View to use as an informational prompt.
     */
    public void setPromptView(View prompt) {
        boolean showing = isShowing();
        if (showing) {
            removePromptView();
        }
        mPromptView = prompt;
        if (showing) {
            show();
        }
    }

    /**
     * Post a {@link #show()} call to the UI thread.
     */
    public void postShow() {
        mHandler.post(mShowDropDownRunnable);
    }
	
    /**
     * Show the popup list. If the list is already showing, this method will recalculate the popup's
     * size and position.
     */
    public void show() {
		if(isShowing()){
			return;
		}
        int height = buildDropDown();
        int widthSpec = 0;
        int heightSpec = 0;

        boolean noInputMethod = isInputMethodNotNeeded();

        if (mPopupWindow.isShowing()) {
            if (mDropDownWidth == ViewGroup.LayoutParams.FILL_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                widthSpec = -1;
            } else if (mDropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                widthSpec = getAnchorView().getWidth();
            } else {
                widthSpec = mDropDownWidth;
            }

            if (mDropDownHeight == ViewGroup.LayoutParams.FILL_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                heightSpec = noInputMethod ? height : ViewGroup.LayoutParams.FILL_PARENT;
                if (noInputMethod) {
                	mPopupWindow.setWindowLayoutMode(
                            mDropDownWidth == ViewGroup.LayoutParams.FILL_PARENT ?
                                    ViewGroup.LayoutParams.FILL_PARENT : 0, 0);
                } else {
                	mPopupWindow.setWindowLayoutMode(
                            mDropDownWidth == ViewGroup.LayoutParams.FILL_PARENT ?
                                    ViewGroup.LayoutParams.FILL_PARENT : 0,
                            ViewGroup.LayoutParams.FILL_PARENT);
                }
            } else if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = height;
            } else {
                heightSpec = mDropDownHeight;
            }

            mPopupWindow.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);

            if(!mStatusBarVisiable) {
            	mPopupWindow.update(getAnchorView(), mDropDownHorizontalOffset,
            			mDropDownVerticalOffset, widthSpec, heightSpec);
            	
            } else {
            	mPopupWindow.showAtLocation(getAnchorView(), Gravity.CENTER, 0, 0);
            }
            
        } else {
            if (mDropDownWidth == ViewGroup.LayoutParams.FILL_PARENT) {
                widthSpec = ViewGroup.LayoutParams.FILL_PARENT;
            } else {
                if (mDropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                	mPopupWindow.setWidth(getAnchorView().getWidth());
                } else {
                	mPopupWindow.setWidth(mDropDownWidth);
                }
            }

            if (mDropDownHeight == ViewGroup.LayoutParams.FILL_PARENT) {
                heightSpec = ViewGroup.LayoutParams.FILL_PARENT;
            } else {
                if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                	mPopupWindow.setHeight(height);
                } else {
                	mPopupWindow.setHeight(mDropDownHeight);
                }
            }

            mPopupWindow.setWindowLayoutMode(widthSpec, heightSpec);

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            mPopupWindow.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);
            mPopupWindow.setTouchInterceptor(mTouchInterceptor);
            
            if(!mStatusBarVisiable) {
            	mPopupWindow.showAtLocation(getAnchorView(), Gravity.TOP | Gravity.RIGHT, mDropDownHorizontalOffset, mDropDownVerticalOffset);
            } else {
            	mPopupWindow.showAtLocation(getAnchorView(), Gravity.CENTER, 0, 0);
            }
            /*mPopupWindow.showAsDropDown(getAnchorView(),
                    mDropDownHorizontalOffset, mDropDownVerticalOffset);*/
            mDropDownList.setSelection(ListView.INVALID_POSITION);

            if (!mModal || mDropDownList.isInTouchMode()) {
                clearListSelection();
            }
            if (!mModal) {
                mHandler.post(mHideSelector);
            }
        }
    }

	
	
    /**
     * <p>Builds the popup window's content and returns the height the popup should have. Returns -1
     * when the content already exists.</p>
     *
     * @return the content's height or -1 if content already exists
     */
    private int buildDropDown() {
        ViewGroup dropDownView;
        int otherHeights = 0;

        if (mDropDownList == null) {
            Context context = mContext;

            /**
             * This Runnable exists for the sole purpose of checking if the view layout has got
             * completed and if so call showDropDown to display the drop down. This is used to show
             * the drop down as soon as possible after user opens up the search dialog, without
             * waiting for the normal UI pipeline to do it's job which is slower than this method.
             */
            mShowDropDownRunnable = new Runnable() {
                public void run() {
                    // View layout should be all done before displaying the drop down.
                    View view = getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        show();
                    }
                }
            };

            mDropDownList = new CCPDropDownListView(context, !mModal);
            if (mDropDownListHighlight != null) {
                mDropDownList.setSelector(mDropDownListHighlight);
            }
            mDropDownList.setAdapter(mListAdapter);
            mDropDownList.setOnItemClickListener(mItemClickListener);
            mDropDownList.setFocusable(true);
            mDropDownList.setFocusableInTouchMode(true);
            mDropDownList.setDivider(null);
            mDropDownList.setDividerHeight(0);
            mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {

                    if (position != -1) {
                        CCPDropDownListView dropDownList = mDropDownList;

                        if (dropDownList != null) {
                            dropDownList.mListSelectionHidden = false;
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            mDropDownList.setOnScrollListener(mScrollListener);

            if (mOnItemSelectedListener != null) {
                mDropDownList.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            dropDownView = mDropDownList;

            View hintView = mPromptView;
            if (hintView != null) {
                // if a hint has been specified, we accomodate more space for it and
                // add a text view in the drop down menu, at the bottom of the list
                LinearLayout hintContainer = new LinearLayout(context);
                hintContainer.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 0, 1.0f
                );

                switch (mPromptPosition) {
                    case POSITION_PROMPT_BELOW:
                        hintContainer.addView(dropDownView, hintParams);
                        hintContainer.addView(hintView);
                        break;

                    case POSITION_PROMPT_ABOVE:
                        hintContainer.addView(hintView);
                        hintContainer.addView(dropDownView, hintParams);
                        break;

                    default:
                        break;
                }

                // measure the hint's height to find how much more vertical space
                // we need to add to the drop down's height
                int widthSpec = MeasureSpec.makeMeasureSpec(mDropDownWidth, MeasureSpec.AT_MOST);
                int heightSpec = MeasureSpec.UNSPECIFIED;
                hintView.measure(widthSpec, heightSpec);

                hintParams = (LinearLayout.LayoutParams) hintView.getLayoutParams();
                otherHeights = hintView.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;

                dropDownView = hintContainer;
            }

            mPopupWindow.setContentView(dropDownView);
        } else {
            dropDownView = (ViewGroup) mPopupWindow.getContentView();
            final View view = mPromptView;
            if (view != null) {
                LinearLayout.LayoutParams hintParams =
                        (LinearLayout.LayoutParams) view.getLayoutParams();
                otherHeights = view.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;
            }
        }

        // getMaxAvailableHeight() subtracts the padding, so we put it back
        // to get the available height for the whole window
        int padding = 0;
        Drawable background = mPopupWindow.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            padding = mTempRect.top + mTempRect.bottom;

            // If we don't have an explicit vertical offset, determine one from the window
            // background so that content will line up.
            if (!mDropDownVerticalOffsetSet) {
                mDropDownVerticalOffset = -mTempRect.top;
            }
        } else {
            mTempRect.setEmpty();
        }

        // Max height available on the screen for a popup.
        boolean ignoreBottomDecorations =
        	mPopupWindow.getInputMethodMode() == PopupWindow.INPUT_METHOD_NOT_NEEDED;
        final int maxHeight = getMaxAvailableHeight(
                getAnchorView(), mDropDownVerticalOffset, ignoreBottomDecorations);

        if (mDropDownAlwaysVisible || mDropDownHeight == ViewGroup.LayoutParams.FILL_PARENT) {
            return maxHeight + padding;
        }

        final int childWidthSpec;
        switch (mDropDownWidth) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                        mContext.getResources().getDisplayMetrics().widthPixels -
                                (mTempRect.left + mTempRect.right),
                        MeasureSpec.AT_MOST);
                
                break;
            case ViewGroup.LayoutParams.FILL_PARENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                        mContext.getResources().getDisplayMetrics().widthPixels -
                                (mTempRect.left + mTempRect.right),
                        MeasureSpec.EXACTLY);
                break;
            default:
                childWidthSpec = MeasureSpec.makeMeasureSpec(mDropDownWidth, MeasureSpec.EXACTLY);
                break;
        }

        final int listContent = mDropDownList.measureHeightOfChildrenCompat(childWidthSpec,
                0, CCPDropDownListView.NO_POSITION, maxHeight - otherHeights, -1);
        // add padding only if the list has items in it, that way we don't show
        // the popup if it is not needed
        if (listContent > 0) {
            otherHeights += padding;
        }

        return listContent + otherHeights;
    }

    /**
     * Copied from PopupWindow.java of JB
     *
     * Returns the maximum height that is available for the popup to be completely shown, optionally
     * ignoring any bottom decorations such as the input method. It is recommended that this height
     * be the maximum for the popup's height, otherwise it is possible that the popup will be
     * clipped.
     *
     * @param anchor                  The view on which the popup window must be anchored.
     * @param yOffset                 y offset from the view's bottom edge
     * @param ignoreBottomDecorations if true, the height returned will be all the way to the bottom
     *                                of the display, ignoring any bottom decorations
     * @return The maximum available height for the popup to be completely shown.
     * @hide Pending API council approval.
     */
    public int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        final Rect displayFrame = new Rect();
        anchor.getWindowVisibleDisplayFrame(displayFrame);

        int[] mDrawingLocation = new int[2];
        final int[] anchorPos = mDrawingLocation;
        anchor.getLocationOnScreen(anchorPos);

        int bottomEdge = displayFrame.bottom;
        if (ignoreBottomDecorations) {
            Resources res = anchor.getContext().getResources();
            bottomEdge = res.getDisplayMetrics().heightPixels;
        }
        
        // modify by 2014/6/9 for action overflow height
        final int distanceToBottom = bottomEdge - (anchorPos[1] /*+ anchor.getHeight()*/) - yOffset;
        final int distanceToTop = anchorPos[1] - displayFrame.top + yOffset;

        // anchorPos[1] is distance from anchor to top of screen
        int returnedHeight = Math.max(distanceToBottom, distanceToTop);
        if (mPopupWindow.getBackground() != null) {
        	mPopupWindow.getBackground().getPadding(mTempRect);
            returnedHeight -= mTempRect.top + mTempRect.bottom;
        }

        return returnedHeight;
    }
	
	public class PopupDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            if (isShowing()) {
                // Resize the popup to fit new content
                show();
            }
        }

        @Override
        public void onInvalidated() {
            dismiss();
        }
    }
	
    private class ListSelectorHider implements Runnable {

        public void run() {
            clearListSelection();
        }
    }
	
	
	public class ResizePopupRunnable implements Runnable {

		private CCPListPopupWindow mWindow;

		/**
		 * 
		 * @param window
		 */
		public ResizePopupRunnable(CCPListPopupWindow window) {
			mWindow = window;
		}
		
		@Override
		public void run() {
			if(mDropDownList == null
					|| mDropDownList.getCount() <= mDropDownList.getChildCount()
					|| mDropDownList.getChildCount() > mDropDownVerticalOffset) {
				return;
			}
			mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
			mWindow.show();
		}
		
	}
	
    private class PopupTouchInterceptor implements OnTouchListener {

        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            if (action == MotionEvent.ACTION_DOWN &&
                    mPopupWindow != null && mPopupWindow.isShowing() &&
                    (x >= 0 && x < mPopupWindow.getWidth() && y >= 0 && y < mPopupWindow.getHeight())) {
                mHandler.postDelayed(mResizePopupRunnable, EXPAND_LIST_TIMEOUT);
            } else if (action == MotionEvent.ACTION_UP) {
                mHandler.removeCallbacks(mResizePopupRunnable);
            }
            return false;
        }
    }
    
	
	public class OnListScrollListener implements AbsListView.OnScrollListener {

		private CCPListPopupWindow mWindow;
		
		/**
		 * 
		 * @param window
		 */
		public OnListScrollListener(CCPListPopupWindow window) {
			mWindow = window;
		}
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
			if(scrollState != SCROLL_STATE_TOUCH_SCROLL
					|| mWindow.isInputMethodNotNeeded()
					|| mPopupWindow.getContentView() == null) {
				return ;
			}
			mHandler.removeCallbacks(mResizePopupRunnable);
			mResizePopupRunnable.run();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
		}
		
	}
	
	public class OnListItemSelectedListener implements AdapterView.OnItemSelectedListener {
		private CCPListPopupWindow mWindow;
		
		/**
		 * 
		 * @param window
		 */
		public OnListItemSelectedListener(CCPListPopupWindow window) {
			mWindow = window;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			if(position == -1) {
				return ;
			}
			
			CCPPopupListView listView = (CCPPopupListView) mWindow.getListView();
			if(listView == null) {
				return;
			}
			
			listView.setRequestFocusable(false);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	}
	
	
    /**
     * <p>Wrapper class for a ListView. This wrapper can hijack the focus to make sure the list uses
     * the appropriate drawables and states when displayed on screen within a drop down. The focus
     * is never actually passed to the drop down in this mode; the list only looks focused.</p>
     */
    private static class CCPDropDownListView extends ListView {

        private static final String TAG = CCPListPopupWindow.TAG + ".DropDownListView";

        /*
        * WARNING: This is a workaround for a touch mode issue.
        *
        * Touch mode is propagated lazily to windows. This causes problems in
        * the following scenario:
        * - Type something in the AutoCompleteTextView and get some results
        * - Move down with the d-pad to select an item in the list
        * - Move up with the d-pad until the selection disappears
        * - Type more text in the AutoCompleteTextView *using the soft keyboard*
        *   and get new results; you are now in touch mode
        * - The selection comes back on the first item in the list, even though
        *   the list is supposed to be in touch mode
        *
        * Using the soft keyboard triggers the touch mode change but that change
        * is propagated to our window only after the first list layout, therefore
        * after the list attempts to resurrect the selection.
        *
        * The trick to work around this issue is to pretend the list is in touch
        * mode when we know that the selection should not appear, that is when
        * we know the user moved the selection away from the list.
        *
        * This boolean is set to true whenever we explicitly hide the list's
        * selection and reset to false whenever we know the user moved the
        * selection back to the list.
        *
        * When this boolean is true, isInTouchMode() returns true, otherwise it
        * returns super.isInTouchMode().
        */
        private boolean mListSelectionHidden;


        public static final int INVALID_POSITION = -1;

        static final int NO_POSITION = -1;


        /**
         * True if this wrapper should fake focus.
         */
        private boolean mHijackFocus;

        /**
         * <p>Creates a new list view wrapper.</p>
         *
         * @param context this view's context
         */
        public CCPDropDownListView(Context context, boolean hijackFocus) {
            super(context, null, R.attr.dropDownListViewStyle);
            mHijackFocus = hijackFocus;
            setCacheColorHint(0); // Transparent, since the background drawable could be anything.
        }

        /**
         * Find a position that can be selected (i.e., is not a separator).
         *
         * @param position The starting position to look at.
         * @param lookDown Whether to look down for other positions.
         * @return The next selectable position starting at position and then searching either up or
         *         down. Returns {@link #INVALID_POSITION} if nothing can be found.
         */
        private int lookForSelectablePosition(int position, boolean lookDown) {
            final ListAdapter adapter = getAdapter();
            if (adapter == null || isInTouchMode()) {
                return INVALID_POSITION;
            }

            final int count = adapter.getCount();
            if (!getAdapter().areAllItemsEnabled()) {
                if (lookDown) {
                    position = Math.max(0, position);
                    while (position < count && !adapter.isEnabled(position)) {
                        position++;
                    }
                } else {
                    position = Math.min(position, count - 1);
                    while (position >= 0 && !adapter.isEnabled(position)) {
                        position--;
                    }
                }

                if (position < 0 || position >= count) {
                    return INVALID_POSITION;
                }
                return position;
            } else {
                if (position < 0 || position >= count) {
                    return INVALID_POSITION;
                }
                return position;
            }
        }
        

        @Override
        public boolean isInTouchMode() {
            // WARNING: Please read the comment where mListSelectionHidden is declared
            return (mHijackFocus && mListSelectionHidden) || super.isInTouchMode();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean hasWindowFocus() {
            return mHijackFocus || super.hasWindowFocus();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean isFocused() {
            return mHijackFocus || super.isFocused();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean hasFocus() {
            return mHijackFocus || super.hasFocus();
        }

        /**
         * Measures the height of the given range of children (inclusive) and returns the height
         * with this ListView's padding and divider heights included. If maxHeight is provided, the
         * measuring will stop when the current height reaches maxHeight.
         *
         * @param widthMeasureSpec             The width measure spec to be given to a child's
         *                                     {@link View#measure(int, int)}.
         * @param startPosition                The position of the first child to be shown.
         * @param endPosition                  The (inclusive) position of the last child to be
         *                                     shown. Specify {@link #NO_POSITION} if the last child
         *                                     should be the last available child from the adapter.
         * @param maxHeight                    The maximum height that will be returned (if all the
         *                                     children don't fit in this value, this value will be
         *                                     returned).
         * @param disallowPartialChildPosition In general, whether the returned height should only
         *                                     contain entire children. This is more powerful--it is
         *                                     the first inclusive position at which partial
         *                                     children will not be allowed. Example: it looks nice
         *                                     to have at least 3 completely visible children, and
         *                                     in portrait this will most likely fit; but in
         *                                     landscape there could be times when even 2 children
         *                                     can not be completely shown, so a value of 2
         *                                     (remember, inclusive) would be good (assuming
         *                                     startPosition is 0).
         * @return The height of this ListView with the given children.
         */
        final int measureHeightOfChildrenCompat(int widthMeasureSpec, int startPosition,
                int endPosition, final int maxHeight,
                int disallowPartialChildPosition) {

            final int paddingTop = getListPaddingTop();
            final int paddingBottom = getListPaddingBottom();
            final int paddingLeft = getListPaddingLeft();
            final int paddingRight = getListPaddingRight();
            final int reportedDividerHeight = getDividerHeight();
            final Drawable divider = getDivider();

            final ListAdapter adapter = getAdapter();

            if (adapter == null) {
                return paddingTop + paddingBottom;
            }

            // Include the padding of the list
            int returnedHeight = paddingTop + paddingBottom;
            final int dividerHeight = ((reportedDividerHeight > 0) && divider != null)
                    ? reportedDividerHeight : 0;

            // The previous height value that was less than maxHeight and contained
            // no partial children
            int prevHeightWithoutPartialChild = 0;

            View child = null;
            int viewType = 0;
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                int newType = adapter.getItemViewType(i);
                if (newType != viewType) {
                    child = null;
                    viewType = newType;
                }
                child = adapter.getView(i, child, this);
                ;

                // Compute child height spec
                int heightMeasureSpec;
                int childHeight = child.getLayoutParams().height;
                if (childHeight > 0) {
                    heightMeasureSpec = MeasureSpec
                            .makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                } else {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
                child.measure(widthMeasureSpec, heightMeasureSpec);

                if (i > 0) {
                    // Count the divider for all but one child
                    returnedHeight += dividerHeight;
                }

                returnedHeight += child.getMeasuredHeight();

                if (returnedHeight >= maxHeight) {
                    // We went over, figure out which height to return.  If returnedHeight >
                    // maxHeight, then the i'th position did not fit completely.
                    return (disallowPartialChildPosition >= 0) // Disallowing is enabled (> -1)
                            && (i > disallowPartialChildPosition) // We've past the min pos
                            && (prevHeightWithoutPartialChild > 0) // We have a prev height
                            && (returnedHeight != maxHeight) // i'th child did not fit completely
                            ? prevHeightWithoutPartialChild
                            : maxHeight;
                }

                if ((disallowPartialChildPosition >= 0) && (i >= disallowPartialChildPosition)) {
                    prevHeightWithoutPartialChild = returnedHeight;
                }
            }

            // At this point, we went through the range of children, and they each
            // completely fit, so return the returnedHeight
            return returnedHeight;
        }

    }
}
