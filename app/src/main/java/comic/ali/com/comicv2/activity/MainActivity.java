/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;

import comic.ali.com.comicv2.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import comic.ali.com.comicv2.fragment.HomeFragment;
import comic.ali.com.comicv2.renderer.TabCategoryListener;
import comic.ali.com.comicv2.renderer.TabPagerAdapter;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class MainActivity extends SherlockFragmentActivity {
    private SherlockFragmentActivity mTabHost;
    public static Context context;
    public ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        final TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

        // setup action bar for tabs
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        mViewPager = (ViewPager) findViewById(R.id.home_pager);
        mViewPager.setAdapter(tabPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab()
                .setText("Home")
                .setTabListener(new TabCategoryListener<HomeFragment>(this, HomeFragment.class, mViewPager)));
    }
}
