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
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import comic.ali.com.comicv2.DraggablePanelApplication;
import comic.ali.com.comicv2.R;

import comic.ali.com.comicv2.model.User;
import comic.ali.com.comicv2.renderer.TabPagerAdapter;
import comic.ali.com.comicv2.tools.Analytics;


/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class MainActivity extends FragmentActivity {
    public static Context context;
    public ViewPager mViewPager;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


//        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();

        final TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.home_pager);
        mViewPager.setAdapter(tabPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //page favorite
                if(position == MainActivity.context.getResources().getInteger(R.integer.home_tab_favorite)) {
                    User user = new User(MainActivity.context);
                    //check login
                    if(!user.isLogin()) {
                        Intent myIntent = new Intent(MainActivity.context, LoginActivity.class);
                        MainActivity.context.startActivity(myIntent);
                    }
                }
            }
        });


        //Get a Tracker (should auto-report)
        Tracker t = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("Home");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts and uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User user = new User(this);
        if(user.isLogin()) {
            getMenuInflater().inflate(R.menu.main_loginned, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_default, menu);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks START ALARM, set the alarm.
            case R.id.login:
                Intent myIntent = new Intent(MainActivity.context, LoginActivity.class);
                MainActivity.context.startActivity(myIntent);
                return true;
            case R.id.logout:
                User user = new User(this);
                user.logout();
                this.reset();
            case R.id.action_search:
                myIntent = new Intent(MainActivity.context, SearchActivity.class);
                MainActivity.context.startActivity(myIntent);
                return true;
        }
        return false;
    }

    private void reset() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
