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
package comic.ali.com.truyentranh.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import android.widget.SearchView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.model.Manga;
import comic.ali.com.truyentranh.model.User;
import comic.ali.com.truyentranh.renderer.GridMangaAdapter;
import comic.ali.com.truyentranh.tools.Analytics;
import comic.ali.com.truyentranh.tools.CallAPI;
import comic.ali.com.truyentranh.viewmodel.MyGridView;
import comic.ali.com.truyentranh.viewmodel.ObservableScrollView;
import comic.ali.com.truyentranh.viewmodel.ScrollViewListener;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener, ScrollViewListener {
    public static Context context;
    Menu menu;
    MyGridView tvShowsGridView;
    GridMangaAdapter adapter;
    String api;
    public static Boolean loadingMore = false;
    ObservableScrollView scrollView;
    SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.home_favorite);
        context = this;


//        handleIntent(getIntent());

        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.setScrollViewListener(this);


        //Get a Tracker (should auto-report)
        Tracker t = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("Detail");
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
    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
    }

    void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
//        }
    }

    ProgressDialog pDialog;
    int current_page = 1;

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(tvShowsGridView != null && tvShowsGridView.getHeight() > 100) {
            int scrollHeight = scrollView.getHeight() + y;
            int gridViewHeight = tvShowsGridView.getHeight();
            if (scrollHeight >= gridViewHeight && !loadingMore) {
                loadingMore = true;
                //last row
                current_page += 1;
                String url = api + "&page=" + current_page;
                new loadMore().execute(url);
            }
        }
    }

    void LoadMoreData(String result) {
        adapter.addMoreData(result);
        loadingMore = false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        initData(s);
        mSearchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class loadMore extends CallAPI {
        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(SearchActivity.this, getString(R.string.dialog_title), getString(R.string.dialog_loading), true, false);
        }
        @Override
        protected void onPostExecute(String result) {
            LoadMoreData(result);
            pDialog.dismiss();
        }
    } // end CallAPI

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        User user = new User(this);
        inflater.inflate(R.menu.main_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();

        this.menu = menu;
        return true;
    }

    void initData(String query) {
        current_page = 1;
        tvShowsGridView = (MyGridView)findViewById(R.id.gv_tv_shows);
        api = "http://comicvn.net/truyentranh/apiv2/timtruyen?keyword="+query;
        adapter = new GridMangaAdapter(SearchActivity.context, null, api);
        tvShowsGridView.setAdapter(adapter);
        tvShowsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Manga item = (Manga)adapter.getItem(position);
                Intent myIntent = new Intent(SearchActivity.context, DetailActivity.class);
                myIntent.putExtra("detail", item.toString());
                SearchActivity.context.startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
