package comic.ali.com.comicv2.renderer;

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

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.activity.DetailActivity;
import comic.ali.com.comicv2.activity.LoginActivity;
import comic.ali.com.comicv2.activity.MainActivity;
import comic.ali.com.comicv2.fragment.CategoryFragment;
import comic.ali.com.comicv2.fragment.HomeFavoriteFragment;
import comic.ali.com.comicv2.fragment.HomeFragment;

/**
 * Created by AliPro on 2/27/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    final int NUM_ITEMS = 3; // number of tabs
    protected static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
    };
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
