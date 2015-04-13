package com.ali.truyentranh.renderer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

/**
 * Created by AliPro on 2/27/2015.
 */
public class TabCategoryListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final Class<T> mClass;
    private final ViewPager mViewPager;

    /** Constructor used each time a new tab is created.
     * @param activity  The host Activity, used to instantiate the fragment
     * @param clz  The fragment's Class, used to instantiate the fragment
     */
    public TabCategoryListener(Activity activity, Class<T> clz, ViewPager viewPager) {
        mActivity = activity;
        mClass = clz;
        mViewPager = viewPager;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int i = tab.getPosition();
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

}
