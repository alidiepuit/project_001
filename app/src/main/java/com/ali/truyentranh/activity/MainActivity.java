package com.ali.truyentranh.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ali.truyentranh.model.NavDrawerItem;
import com.ali.truyentranh.renderer.NavDrawerListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.ali.truyentranh.R;

import com.ali.truyentranh.model.User;
import com.ali.truyentranh.renderer.TabPagerAdapter;
import com.ali.truyentranh.tools.Analytics;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    public static Context context;
    public ViewPager mViewPager;
    Menu menu;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
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

        initSliceMenu();

        //Get a Tracker (should auto-report)
        Tracker t = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("Home");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void initSliceMenu() {
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(1, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayView(i);
            }
        });
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        Intent myIntent;
        switch (position) {
            case 0:
                myIntent = new Intent(MainActivity.context, NewsActivity.class);
                MainActivity.context.startActivity(myIntent);
                break;
            case 1:
                myIntent = new Intent(MainActivity.context, NewsActivity.class);
                MainActivity.context.startActivity(myIntent);
                break;
            default:
                break;
        }
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

    /* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        User user = new User(this);
        if(user.isLogin()) {
            menu.findItem(R.id.logout).setVisible(!drawerOpen);
        } else {
            menu.findItem(R.id.login).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
