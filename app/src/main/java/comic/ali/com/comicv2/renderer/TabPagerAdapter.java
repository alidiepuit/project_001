package comic.ali.com.comicv2.renderer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.activity.MainActivity;
import comic.ali.com.comicv2.fragment.HomeFragment;

/**
 * Created by AliPro on 2/27/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    final int NUM_ITEMS = 1; // number of tabs

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(HomeFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
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
        }
        return tabLabel;
    }
}
