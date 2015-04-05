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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.renderer.MySliderAdapter;
import comic.ali.com.truyentranh.renderer.TvShowAdapter;
import comic.ali.com.truyentranh.viewmodel.MyGridView;
import comic.ali.com.truyentranh.viewmodel.TvShowViewModel;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Sample activity created to show a list of famous tv shows. If the user clicks on any list
 * element this sample shows a detailed draggable view with a picture and a list of episodes.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TvShowsActivity extends DIFragmentActivity {

    private static final int DELAY_MILLIS = 10;

    @Inject
    TvShowAdapter adapter;

    MyGridView tvShowsGridView;

    TextView header;
    public static Context context;
    public static View viewManga;

    private TvShowViewModel tvShowSelected;

    /**
     * Initialize the Activity with some injected data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_sample);
        this.context = this;
        ButterKnife.inject(this);
        initializeGridView();

        MySliderAdapter slider = new MySliderAdapter(this);

        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Initialize GridView with some injected data and configure OnItemClickListener.
     */
    public void initializeGridView() {
        tvShowsGridView = (MyGridView)findViewById(R.id.gv_tv_shows);
        tvShowsGridView.setAdapter(adapter);
        tvShowsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                TvShowViewModel item = adapter.getItem(position);
                Intent myIntent = new Intent(TvShowsActivity.this, DetailActivity.class);
                myIntent.putExtra("detail", item.toString());
                TvShowsActivity.this.startActivity(myIntent);
            }
        });
    }

    /**
     * Update action bar title with the default title value.
     */
    private void resetActionBarTitle() {
        tvShowSelected = null;
        getSupportActionBar().setTitle(R.string.app_name);
    }

    /**
     * Update action bar title with the tv show selected title.
     */
    private void updateActionBarTitle() {
        if (tvShowSelected != null) {
            getSupportActionBar().setTitle(tvShowSelected.getTitle());
        }
    }


}