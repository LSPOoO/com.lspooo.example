package com.lspooo.plugin.common.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lspooo.plugin.common.R;
import com.lspooo.plugin.common.common.bar.StatusBarCompat;
import com.lspooo.plugin.common.common.menu.ActionMenuItem;
import com.lspooo.plugin.common.common.menu.search.SearchViewHelper;
import com.lspooo.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.plugin.common.tools.SystemBarTintManager;

import java.util.LinkedList;

/**
 * Created by LSP on 2017/9/21.
 */

public abstract class BaseActivity extends AbsActivity{

    private LayoutInflater mLayoutInflater;
    private SystemBarTintManager mSystemBarTintManager;

    protected View mBaseLayoutView;
    private View mTransLayerView;
    private FrameLayout mContentFrameLayout;
    protected View mContentView;

    private ActionBar mActionBar;
    private Toolbar mToolBar;
    private View mActionBarLayout;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mTitleSummaryView;
    private View mActionBarShadow;

    private CharSequence mTitleText;
    private boolean mSearchEnabled;
    private SearchViewHelper mSearchViewHelper;

    private ActionMenuItem mDisplayHomeMenu = new ActionMenuItem();
    private LinkedList<ActionMenuItem> mActionMenuItems = new LinkedList<>();
    private ActionMenuItem mOverFlowAction;
    private MenuItem mOverFlowMenuItem;
    private int mSmallPadding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!allowPlenaryContentView()){
            return;
        }
        init();
    }

    private void init() {
        mSystemBarTintManager = new SystemBarTintManager(this);
        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(this);
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.lsp_activity, null);

        mTransLayerView = mBaseLayoutView.findViewById(R.id.ytx_trans_layer);
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ytx_content_fl);
        ViewGroup mRootView = (ViewGroup) mBaseLayoutView.findViewById(R.id.ytx_root_view);

        if (hasActionBar()){
            mActionBarLayout = mLayoutInflater.inflate(R.layout.recycler_view_toolbar, null);
            Toolbar toolbar = (Toolbar) mActionBarLayout.findViewById(R.id.toolbar);
            mTitleView = (TextView) mActionBarLayout.findViewById(R.id.action_title);
            mSubTitleView = (TextView) mActionBarLayout.findViewById(R.id.action_sub_title);
            mTitleSummaryView = (TextView) mActionBarLayout.findViewById(R.id.action_title_count);
            mActionBarShadow = mActionBarLayout.findViewById(R.id.toolbar_shadow);
            mRootView.addView(mActionBarLayout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                mToolBar = toolbar;
                if (buildActionBarPadding()) {
                    // 设置状态栏和顶部有一个padding间距
                    mRootView.setPadding(mRootView.getPaddingLeft(), mSystemBarTintManager.getConfig().getStatusBarHeight(), mRootView.getPaddingRight(), mRootView.getPaddingBottom());
                }
                mActionBar = getSupportActionBar();
               /* if (mActionBar != null) {
                    mActionBar.setHomeAsUpIndicator(mActionBarActivity.getResources().getDrawable(R.drawable.ic_arrow_back_24));
                }*/
            }
        }
        if (layoutId != -1) {
            mContentView = mLayoutInflater.inflate(layoutId, null);
            mRootView.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
        setContentView(mBaseLayoutView);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.action_bar_color));
        initSwipeLayout();
        setNavigationOnClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onDisplayHomeAsUp();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (createOptionsMenu(menu)) {
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        prepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return optionsItemSelected(item);
    }

    /**
     * 隐藏 状态栏
     */
    final void hideActionBar() {
        if (mActionBar != null) {
            mActionBar.hide();
        }
        if (mActionBarShadow != null) mActionBarShadow.setVisibility(View.GONE);
    }

    /**
     * 显示状态栏
     */
    final void showActionBar() {
        if (mActionBar != null) {
            mActionBar.show();
        }
        if (mActionBarShadow != null) mActionBarShadow.setVisibility(View.VISIBLE);
    }

    /**
     * 设置当前界面的描述
     * @param contentDescription 当前界面的描述
     */
    public void setContentViewDescription(CharSequence contentDescription) {
        if (TextUtils.isEmpty(contentDescription)) {
            return;
        }
        String description = getString(R.string.common_enter_activity) + contentDescription;
        getWindow().getDecorView().setContentDescription(description);
    }

    /**
     * 设置标题栏返回菜单事件监听
     * @param listener 事件监听
     */
    void setNavigationOnClickListener(MenuItem.OnMenuItemClickListener listener) {
        if (mActionBar == null) {
            return;
        }
        if (listener == null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDisplayHomeMenu.setMenuId(android.R.id.home);
        mDisplayHomeMenu.setItemClickListener(listener);
    }

    /**
     * 默认关闭当前页面
     */
    protected void onDisplayHomeAsUp() {
        hideSoftKeyboard();
        finish();
    }

    /**
     * 设置是否显示返回按钮
     * @param enabled 是否可用
     */
    protected void setDisplayHomeActionMenuEnabled(boolean enabled) {
        if (mDisplayHomeMenu == null || mDisplayHomeMenu.isEnabled() == enabled) {
            return;
        }

        mDisplayHomeMenu.setEnabled(enabled);
        invalidateActionMenu();
    }

    /**
     * 设置是否显示返回按钮
     */
    protected void hideHomeActionMenu() {
        if (mActionBar == null) {
            return;
        }
        mActionBar.setDisplayHomeAsUpEnabled(false);
    }

    /**
     * 设置状态栏标题
     * @param title 标题
     */
    protected final void setActionBarTitle(CharSequence title) {
        if (mActionBar == null) {
            return;
        }
        mTitleText = title;
        onBuildActionBarTitle(title, mActionBar);
        setContentViewDescription(title);
    }

    private void onBuildActionBarTitle(CharSequence title, ActionBar bar) {
        if (isActionBarTitleMiddle()) {
            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
            return;
        }
        if (mActionBar != null) {
            mActionBar.setTitle(title);
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }


    /**
     * 设置状态栏子标题
     * @param title 标题内容
     */
    protected final void setActionBarSubTitle(CharSequence title) {
        if (isActionBarTitleMiddle()) {
            mSubTitleView.setText(title);
            mSubTitleView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
            return;
        }
        if (mActionBar == null) {
            return;
        }
        mActionBar.setSubtitle(title);
        mActionBar.setDisplayShowTitleEnabled(true);
    }

    /**
     * 设置显示标题文字后面追加显示，
     *
     * @param summary 补充文字
     */
    protected final void setActionBarSupplementTitle(CharSequence summary) {
        if (isActionBarTitleMiddle()) {
            if(summary == null || summary.length() == 0) {
                mTitleSummaryView.setVisibility(View.GONE);
                return ;
            }
            mTitleSummaryView.setText(summary);
            mTitleSummaryView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 设置状态栏下部阴影是否显示
     * @param show 是否显示
     */
    protected final void setActionBarShadowVisibility(boolean show) {
        mActionBarShadow.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 重新绘制状态栏
     */
    private final void invalidateActionMenu() {
        supportInvalidateOptionsMenu();
    }

    /**
     * 返回状态栏
     * @return 状态栏
     */
    protected final Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * 返回当前界面加载的跟布局
     * @return 当前界面加载的跟布局
     * @see #getLayoutId()
     */
    protected View getActivityLayoutView() {
        return mContentView;
    }

    /**
     * 返回当前设置到窗口的根布局
     * @return 设置到窗口的根布局
     */
    protected View getContentView() {
        return mBaseLayoutView;
    }

    /**
     * 当前标题栏的文本内容
     * @return 标题栏的文本内容
     */
    protected final CharSequence getActionBarTitle() {
        return mTitleText;
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单
     * @return 结果
     */
    private boolean createOptionsMenu(Menu menu) {
        if (mActionBar == null || mActionMenuItems.size() == 0) {
            return false;
        }
        mOverFlowAction = null;
        mOverFlowMenuItem = null;
        mSmallPadding = getResources().getDimensionPixelSize(R.dimen.SmallPadding);
        int height = mActionBar.getHeight();
        int minimumHeight = height;
        if (height == 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
                minimumHeight = getResources().getDimensionPixelSize(R.dimen.DefaultActionbarHeightPort);
            } else {
                minimumHeight = getResources().getDimensionPixelSize(R.dimen.DefaultActionbarHeightLand);
            }
        }

        for (final ActionMenuItem actionMenuItem : mActionMenuItems) {
            if (actionMenuItem.getMenuId() == android.R.id.home) {
                continue;
            } else if (actionMenuItem.getMenuId() == R.id.ytx_action_bar_search) {
                boolean enabled = this.mSearchEnabled;
                boolean isNil = mSearchViewHelper == null;
                if (enabled && !isNil) {
                    mSearchViewHelper.onCreateOptionsMenu(this, menu);
                }
            } else {
                final MenuItem menuItem = menu.add(0, actionMenuItem.getMenuId(), 0, actionMenuItem.getTitle());
                String className = this.getClass().getName();
                if (menuItem == null) {
                } else if (menuItem.getTitleCondensed() == null) {
                    menuItem.setTitleCondensed("");
                } else if (!(menuItem.getTitleCondensed() instanceof String)) {
                    menuItem.setTitleCondensed(menuItem.getTitleCondensed().toString());
                }
                ActionMenuOnClickListener mClickListener = new ActionMenuOnClickListener(menuItem, actionMenuItem);
                ActionMenuOnLongClickListener mLongClickListener = new ActionMenuOnLongClickListener(actionMenuItem);
                if (actionMenuItem.getResId() != 0) {
                    ImageButton button;
                    if (actionMenuItem.getLongClickListener() != null) {
                        int minWidth = BackwardSupportUtil.fromDPToPix(this, 56);
                        if (actionMenuItem.getLongClickCustomView() == null) {
                            button = new ImageButton(this);
                            button.setLayoutParams(new ViewGroup.MarginLayoutParams(minWidth, minimumHeight));
                            button.setBackgroundResource(R.drawable.actionbar_menu_selector);
                            button.setMinimumHeight(minimumHeight);
                            button.setMinimumWidth(minWidth);
                            button.setImageResource(actionMenuItem.getResId());

                            MenuItemCompat.setActionView(menuItem, button);
                            button.getLayoutParams().width = minWidth;
                            button.getLayoutParams().height = minimumHeight;
                            button.setOnClickListener(mClickListener);
                            button.setOnLongClickListener(mLongClickListener);
                            button.setEnabled(actionMenuItem.isEnabled());
                            button.setContentDescription(actionMenuItem.getTitle());

                            actionMenuItem.setLongClickCustomView(button);
                        }
                        menuItem.setEnabled(actionMenuItem.isEnabled());
                        menuItem.setVisible(actionMenuItem.isVisible());
                    } else {
                        menuItem.setIcon(actionMenuItem.getResId());
                        menuItem.setEnabled(actionMenuItem.isEnabled());
                        menuItem.setVisible(actionMenuItem.isVisible());
                        menuItem.setTitle(menuItem.getTitle());
                        menuItem.setOnMenuItemClickListener(actionMenuItem.getItemClickListener());
                        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
                        if (actionMenuItem.getResId() == R.drawable.ic_more_white) {
                            mOverFlowAction = actionMenuItem;
                            mOverFlowMenuItem = menuItem;
                        }
                    }
                } else {
                    if (actionMenuItem.getCustomView() == null) {
                        actionMenuItem.setCustomView(View.inflate(this, R.layout.action_option_view, null));
                    }

                    TextView mNormalAction;
                    if (actionMenuItem.getActionType() == ActionMenuItem.ActionType.BUTTON) {
                        mNormalAction = (TextView) actionMenuItem.getCustomView().findViewById(R.id.action_option_style_button);
                        actionMenuItem.getCustomView().findViewById(R.id.divider).setVisibility(View.GONE);
                        actionMenuItem.getCustomView().findViewById(R.id.action_option_button).setVisibility(View.GONE);
                        mNormalAction.setPadding(mSmallPadding, 0, mSmallPadding, 0);
                    } else {
                        mNormalAction = (TextView) actionMenuItem.getCustomView().findViewById(R.id.action_option_button);
                        actionMenuItem.getCustomView().findViewById(R.id.divider).setVisibility(View.GONE);
                        actionMenuItem.getCustomView().findViewById(R.id.action_option_button).setVisibility(View.GONE);
                        mNormalAction.setBackgroundResource(R.drawable.actionbar_menu_selector);
                    }
                    mNormalAction.setVisibility(View.VISIBLE);
                    mNormalAction.setText(actionMenuItem.getTitle());
                    mNormalAction.setOnClickListener(mClickListener);
                    mNormalAction.setOnLongClickListener(mLongClickListener);
                    mNormalAction.setEnabled(actionMenuItem.isEnabled());
                    mNormalAction.setVisibility(actionMenuItem.isVisible() ? View.VISIBLE : View.GONE);
                    MenuItemCompat.setActionView(menuItem, actionMenuItem.getCustomView());
                    MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
                }
            }
        }
        return true;
    }

    /**
     * 菜单创建准备工作
     *
     * @param menu 菜单
     */
    private boolean prepareOptionsMenu(Menu menu) {
        if (mSearchEnabled && mSearchViewHelper != null) {
            mSearchViewHelper.onPrepareOptionsMenu(this, menu);
        }
        return true;
    }

    /**
     * 菜单选择事件
     * @param item 菜单
     * @return 是否处理
     */
    private boolean optionsItemSelected(MenuItem item) {

        if (item.getItemId() == mDisplayHomeMenu.getMenuId() && mDisplayHomeMenu.isEnabled()) {
            callMenuCallback(item, mDisplayHomeMenu);
            return true;
        }

        for (ActionMenuItem actionMenuItem : mActionMenuItems) {
            if (item.getItemId() != actionMenuItem.getMenuId()) {
                continue;
            }
            callMenuCallback(item, actionMenuItem);
            return true;
        }

        return false;
    }

    /**
     * 设置状态栏右边显示按钮
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示内容
     * @param resId             按钮背景
     * @param itemClickListener 按钮事件回调监听
     */
    protected final void setActionMenuItem(int menuId, int title, int resId, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, getString(title), itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    protected final void setActionMenuItem(int menuId, int resId, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, "", itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    protected final void setActionMenuItem(int menuId, int resId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, title, itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 设置状态栏右边显示按钮
     *
     * @param title             标题文本
     * @param resId             资源图片
     * @param itemClickListener 按钮事件回调监听
     * @param longClickListener 按钮长按事件回调
     */
    protected final void setActionMenuItem(int title, int resId, MenuItem.OnMenuItemClickListener itemClickListener, View.OnLongClickListener longClickListener) {
        this.setActionMenuItem(0, resId, getString(title), itemClickListener, longClickListener, ActionMenuItem.ActionType.TEXT);
    }


    /**
     * 设置文字显示actionBar
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示文本内容
     * @param itemClickListener 按钮事件回调监听
     */
    protected final void setActionMenuItem(int menuId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, 0, title, itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }


    /**
     * 设置状态栏右边显示按钮
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示文本内容
     * @param itemClickListener 按钮事件回调监听
     * @param actionType        当前按钮显示风格
     */
    protected final void setActionMenuItem(int menuId, String title, MenuItem.OnMenuItemClickListener itemClickListener, ActionMenuItem.ActionType actionType) {
        this.setActionMenuItem(menuId, 0, title, itemClickListener, null, actionType);
    }


    /**
     * 根据提供的资源文件构建一个菜单按钮
     *
     * @param menuId            按钮编号
     * @param resId             按钮资源图片（可以试按钮图片、或者是按钮背景）
     * @param title             按钮显示文本
     * @param itemClickListener 按钮点击事件通知
     * @param longClickListener 按钮长按事件通知
     */
    private void setActionMenuItem(int menuId, int resId, String title, MenuItem.OnMenuItemClickListener itemClickListener, View.OnLongClickListener longClickListener, ActionMenuItem.ActionType actionType) {
        ActionMenuItem actionMenuItem = new ActionMenuItem();
        actionMenuItem.setMenuId(menuId);
        actionMenuItem.setResId(resId);
        actionMenuItem.setTitle(title);
        actionMenuItem.setItemClickListener(itemClickListener);
        actionMenuItem.setLongClickListener(longClickListener);
        actionMenuItem.setActionType(actionType);
        if (actionMenuItem.getResId() == R.drawable.ic_more_white) {
            actionMenuItem.setTitle(getString(R.string.app_more));
        }
        for (int i = 0; i < mActionMenuItems.size(); i++) {
            ActionMenuItem _tempItem = mActionMenuItems.get(i);
            if (_tempItem.getResId() != menuId) {
                continue;
            }
            mActionMenuItems.remove(i);
        }
        mActionMenuItems.add(actionMenuItem);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                invalidateActionMenu();
            }
        }, 200L);
    }

    /**
     * 根据菜单派发菜单事件回调
     * @param menuItem       菜单
     * @param actionMenuItem 菜单动作
     */
    private void callMenuCallback(MenuItem menuItem, ActionMenuItem actionMenuItem) {
        if (actionMenuItem.getItemClickListener() == null) {
            return;
        }
        actionMenuItem.getItemClickListener().onMenuItemClick(menuItem);
    }

    /**
     * 设置单个按钮是否可用状态
     * @param menuId  按钮菜单ID
     * @param enabled 按钮是否可用
     */
    protected void setSingleActionMenuItemEnabled(int menuId, boolean enabled) {
        setActionMenuItemEnabled(false, menuId, enabled);
    }

    /**
     * 设置按钮是否可用状态
     * @param enabled 是否可用
     */
    protected void setAllActionBarMenuItemEnabled(boolean enabled) {
        setActionMenuItemEnabled(true, -1, enabled);
    }

    /**
     * 设置菜单是否可用
     *
     * @param all     是否所有的菜单
     * @param menuId  菜单编号
     * @param enabled 菜单是否可用
     */
    private void setActionMenuItemEnabled(boolean all, int menuId, boolean enabled) {
        boolean changed = false;
        for (ActionMenuItem menuItem : mActionMenuItems) {
            if ((menuId == menuItem.getMenuId() && (menuItem.isEnabled() != enabled))) {
                menuItem.setEnabled(enabled);
                changed = true;
                if (!all) {
                    break;
                }
            } else if (all && menuItem.isEnabled() != enabled) {
                menuItem.setEnabled(enabled);
                changed = true;
            }
        }
        boolean searching = (mSearchViewHelper != null) && mSearchViewHelper.searchViewExpand;
        if (changed) invalidateActionMenu();
    }

    /**
     * 添加一个搜索按钮到状态栏
     *
     * @param enabled 是否可用
     * @param helper  搜索接口
     */
    public void addSearchMenu(boolean enabled, SearchViewHelper helper) {
        ActionMenuItem search = new ActionMenuItem();
        search.setMenuId(R.id.ytx_action_bar_search);
        search.setResId(R.drawable.ic_search_black);
        search.setTitle(getString(R.string.app_search));
        search.setItemClickListener(null);
        search.setLongClickListener(null);
        removeActionMenu(R.id.ytx_action_bar_search);
        mActionMenuItems.add(0, search);
        mSearchEnabled = enabled;
        mSearchViewHelper = helper;
        invalidateActionMenu();
    }

    /**
     * 设置单个按钮是否可显示
     * @param menuId  按钮菜单ID
     * @param enabled 是否可显示
     */
    void setSingleActionMenuItemVisibility(int menuId, boolean enabled) {
        setActionMenuItemVisibility(false, menuId, enabled);
    }

    /**
     * 设置所有按钮是否可显示
     * @param enabled 是否可显示
     */
    void setAllActionBarMenuItemVisibility(boolean enabled) {
        setActionMenuItemVisibility(true, -1, enabled);
    }

    /**
     * 设置菜单是否可以显示
     * @param all        是否全部的菜单
     * @param menuId     菜单编号
     * @param visibility 菜单是否显示
     */
    private void setActionMenuItemVisibility(boolean all, int menuId, boolean visibility) {
        boolean changed = false;
        for (ActionMenuItem menuItem : mActionMenuItems) {

            if ((menuId == menuItem.getMenuId() && (menuItem.isVisible() != visibility))) {
                menuItem.setVisible(visibility);
                if (!all) {
                    changed = true;
                    break;
                }
            } else if (all && menuItem.isVisible() != visibility) {
                menuItem.setVisible(visibility);
                changed = true;
            }
        }
        if (changed) invalidateActionMenu();
    }

    /**
     * 根据菜单编号判断是否已经添加到ToolBar中
     * @param menuId 菜单
     * @return
     */
    public final boolean hasActionMenu(int menuId) {
        return findActionMenuItemById(menuId) != null;
    }

    /**
     * 根据菜单编号查找菜单
     * @param menuId 菜单
     * @return 匹配的菜单
     */
    public ActionMenuItem findActionMenuItemById(int menuId) {
        for (ActionMenuItem item : mActionMenuItems) {
            if (item.getMenuId() == menuId) {
                return item;
            }
        }
        return null;
    }

    /**
     * 根据菜单编号设置菜单显示内容
     *
     * @param menuId 菜单编号
     * @param text   显示内容
     */
    public void setActionMenuText(int menuId, CharSequence text) {
        ActionMenuItem actionMenuItem = findActionMenuItemById(menuId);

        if (actionMenuItem == null || text == null || actionMenuItem.getTitle().equals(text.toString())) {
            return;
        }
        actionMenuItem.setTitle(text);
        invalidateActionMenu();
    }

    /**
     * 删除全部的菜单
     */
    protected void removeAllActionMenuItem() {
        if (mActionMenuItems.isEmpty()) {
            return;
        }
        mActionMenuItems.clear();
        invalidateActionMenu();
    }

    /**
     * 根据菜单的编号删除菜单
     * @param menuId 菜单编号
     * @return 是否删除成功
     */
    protected boolean removeActionMenu(int menuId) {
        for (int i = 0; i < mActionMenuItems.size(); i++) {
            if (mActionMenuItems.get(i).getMenuId() != menuId) {
                continue;
            }
            mActionMenuItems.remove(i);
            invalidateActionMenu();
            return true;
        }
        return false;
    }

    /**
     * 状态栏是否现实状态
     * @return
     */
    public final boolean isActionBarShowing() {
        return mActionBar != null && mActionBar.isShowing();
    }

    /**
     * 返回状态栏的高度
     * @return 状态栏高度
     */
    public final int getActionBarHeight() {
        if (mActionBar == null) {
            return 0;
        }
        return mActionBar.getHeight();
    }

    /**
     * 是否需要全权交由框架处理初始化当前UI主界面
     * 1、会增加初始化状态栏、标题栏
     * 2、会提供默认的设置状态栏的接口等
     *
     * @return 是否需要初始化
     */
    public boolean allowPlenaryContentView() {
        return true;
    }


    public abstract int getLayoutId();
    public abstract boolean hasActionBar();
    public abstract boolean buildActionBarPadding();
    /**
     * 状态栏标题是否剧中
     * @return 是否剧中
     */
    public abstract boolean isActionBarTitleMiddle();


    /**
     * 按钮单击事件处理
     */
    final private class ActionMenuOnClickListener implements View.OnClickListener {

        final private MenuItem mMenuItem;
        final private ActionMenuItem mActionMenuItem;

        /**
         * 按钮事件构造方法
         * @param menuItem       按钮
         * @param actionMenuItem 按钮时间类型
         */
        ActionMenuOnClickListener(MenuItem menuItem, ActionMenuItem actionMenuItem) {
            mMenuItem = menuItem;
            mActionMenuItem = actionMenuItem;
        }

        @Override
        public void onClick(View v) {
            if (mActionMenuItem != null && mActionMenuItem.getItemClickListener() != null) {
                mActionMenuItem.getItemClickListener().onMenuItemClick(mMenuItem);
            }
        }
    }

    /**
     * 按钮长按事件处理
     */
    final private class ActionMenuOnLongClickListener implements View.OnLongClickListener {

        final private ActionMenuItem mActionMenuItem;

        ActionMenuOnLongClickListener(ActionMenuItem actionMenuItem) {
            mActionMenuItem = actionMenuItem;
        }

        @Override
        public boolean onLongClick(View v) {
            if (mActionMenuItem != null && mActionMenuItem.getLongClickListener() != null) {
                mActionMenuItem.getLongClickListener().onLongClick(v);
            }
            return false;
        }


    }
}
