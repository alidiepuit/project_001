/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ali.truyentranh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ali.truyentranh.R;
import com.ali.truyentranh.activity.DetailActivity;
import com.ali.truyentranh.activity.MainActivity;
import com.ali.truyentranh.model.Manga;
import com.ali.truyentranh.renderer.GridHomeFavoriteAdapter;
import com.ali.truyentranh.viewmodel.MyGridView;

public class HomeFavoriteFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    public static View viewTab;
    GridHomeFavoriteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_favorite, container, false);

        this.viewTab = view;
        MyGridView tvShowsGridView = (MyGridView)view.findViewById(R.id.gv_tv_shows);
        adapter = new GridHomeFavoriteAdapter(MainActivity.context, null);
        tvShowsGridView.setAdapter(adapter);
        tvShowsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Manga item = (Manga)adapter.getItem(position);
                Intent myIntent = new Intent(MainActivity.context, DetailActivity.class);
                myIntent.putExtra("detail", item.toString());
                MainActivity.context.startActivity(myIntent);
            }
        });

        return view;
    }
}
