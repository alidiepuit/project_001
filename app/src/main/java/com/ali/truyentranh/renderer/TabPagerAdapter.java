package com.ali.truyentranh.renderer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.ali.truyentranh.R;
import com.ali.truyentranh.activity.DetailActivity;
import com.ali.truyentranh.activity.LoginActivity;
import com.ali.truyentranh.activity.MainActivity;
import com.ali.truyentranh.fragment.CategoryFragment;
import com.ali.truyentranh.fragment.HomeFavoriteFragment;
import com.ali.truyentranh.fragment.HomeFragment;

/**
 * Created by AliPro on 2/27/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    final int NUM_ITEMS = 3; // number of tabs
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Bundle args = new Bundle();
        switch (i) {
            case 0:
                HomeFragment f1 = new HomeFragment();
                args.putInt(HomeFragment.ARG_OBJECT, i);
                f1.setArguments(args);
                return f1;
            case 1:
                CategoryFragment f3 = new CategoryFragment();
                args.putInt(CategoryFragment.ARG_OBJECT, i);
                f3.setArguments(args);
                return f3;
            case 2:
                HomeFavoriteFragment f2 = new HomeFavoriteFragment();
                args.putInt(HomeFavoriteFragment.ARG_OBJECT, i);
                f2.setArguments(args);
                return f2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabLabel = null;
        switch (position) {
            case 0:
                tabLabel = MainActivity.context.getString(R.string.home_tab_1);
                break;
            case 1:
                tabLabel = MainActivity.context.getString(R.string.home_tab_category);
                break;
            case 2:
                tabLabel = MainActivity.context.getString(R.string.home_tab_favorite);
                break;
        }
        return tabLabel;
    }
}
