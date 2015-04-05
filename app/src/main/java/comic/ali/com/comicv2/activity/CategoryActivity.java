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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Category;
import comic.ali.com.comicv2.model.Item;
import comic.ali.com.comicv2.model.Manga;
import comic.ali.com.comicv2.model.User;
import comic.ali.com.comicv2.renderer.GridHomeFavoriteAdapter;
import comic.ali.com.comicv2.renderer.GridMangaAdapter;
import comic.ali.com.comicv2.renderer.TabPagerAdapter;
import comic.ali.com.comicv2.tools.Analytics;
import comic.ali.com.comicv2.tools.CallAPI;
import comic.ali.com.comicv2.viewmodel.MyGridView;
import comic.ali.com.comicv2.viewmodel.ObservableScrollView;
import comic.ali.com.comicv2.viewmodel.ScrollViewListener;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;

public class CategoryActivity extends Activity implements ScrollViewListener {
    public static Context context;
    Menu menu;
    MyGridView tvShowsGridView;
    GridMangaAdapter adapter;
    String api;
    public static Boolean loadingMore = false;
    Boolean stopLoadingData = false;
    ObservableScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_favorite);
        context = this;

        tvShowsGridView = (MyGridView)findViewById(R.id.gv_tv_shows);

        Intent intent = getIntent();
        String value = intent.getStringExtra("category");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        api = "http://comicvn.net/truyentranh/apiv2/theloai?id=";
        Category category = gson.fromJson(value, Category.class);
        api = api + category.getId();
        setTitle(category.getName());

        adapter = new GridMangaAdapter(CategoryActivity.context, null, api);
        tvShowsGridView.setAdapter(adapter);
        tvShowsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Manga item = (Manga)adapter.getItem(position);
                Intent myIntent = new Intent(CategoryActivity.context, DetailActivity.class);
                myIntent.putExtra("detail", item.toString());
                CategoryActivity.context.startActivity(myIntent);
            }
        });

        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.setScrollViewListener(this);


        //Get a Tracker (should auto-report)
        Tracker t = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("Category");
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

    ProgressDialog pDialog;
    int current_page = 1;

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int scrollHeight = scrollView.getHeight()+y;
        int gridViewHeight = tvShowsGridView.getHeight()>100?tvShowsGridView.getHeight():100;
        if(scrollHeight >= gridViewHeight && !loadingMore) {
            loadingMore = true;
            //last row
            current_page += 1;
            String url = api + "&page=" + current_page;
            new loadMore().execute(url);
        }
    }

    void LoadMoreData(String result) {
        adapter.addMoreData(result);
        loadingMore = false;
    }

    public class loadMore extends CallAPI {
        @Override
         protected void onPreExecute() {
            pDialog = ProgressDialog.show(CategoryActivity.this, getString(R.string.dialog_title), getString(R.string.dialog_loading), true, false);
        }
        @Override
        protected void onPostExecute(String result) {
            LoadMoreData(result);
            pDialog.dismiss();
        }
    } // end CallAPI

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
                Intent myIntent = new Intent(CategoryActivity.context, LoginActivity.class);
                CategoryActivity.context.startActivity(myIntent);
                return true;
            case R.id.logout:
                User user = new User(this);
                user.logout();
                this.reset();
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
