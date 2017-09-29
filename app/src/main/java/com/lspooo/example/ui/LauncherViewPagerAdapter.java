package com.lspooo.example.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.lspooo.example.view.LauncherBottomTabView;
import com.lspooo.plugin.common.ui.BaseFragment;

/**
 * Created by LSP on 2017/9/24.
 */

public class LauncherViewPagerAdapter extends FragmentStatePagerAdapter{

    private FragmentActivity context;
    private final SparseArray<Fragment> mTabViewCache = new SparseArray<>();


    public LauncherViewPagerAdapter(FragmentActivity fm) {
        super(fm.getSupportFragmentManager());
        this.context = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return getTabView(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    private BaseFragment getTabView(int tabIndex) {
        if (tabIndex < 0) {
            return null;
        }
        if (mTabViewCache.indexOfKey(tabIndex) >= 0) {
            return (BaseFragment) mTabViewCache.get(tabIndex);
        }
        BaseFragment mFragment = null;
        switch (tabIndex) {
            case LauncherBottomTabView.TAB_COMMUNICATION:
                mFragment = (BaseFragment) Fragment.instantiate(context, TabCommunicationFragment.class.getName(), null);
                break;
            case LauncherBottomTabView.TAB_CONTACT:
                mFragment = (BaseFragment) Fragment.instantiate(context, TabContactFragment.class.getName(), null);
                break;
            case LauncherBottomTabView.TAB_FIND:
                mFragment = (BaseFragment) Fragment.instantiate(context, TabFindFragment.class.getName(), null);
                break;
            case LauncherBottomTabView.TAB_SETTING:
                mFragment = (BaseFragment) Fragment.instantiate(context, TabSettingFragment.class.getName(), null);
                break;
            default:
                break;
        }
        mTabViewCache.put(tabIndex, mFragment);
        return mFragment;
    }

    public void tabSelect(int tabIndex){

    }
}
