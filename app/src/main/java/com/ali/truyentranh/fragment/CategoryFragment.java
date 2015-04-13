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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ali.truyentranh.R;
import com.ali.truyentranh.activity.CategoryActivity;
import com.ali.truyentranh.activity.MainActivity;
import com.ali.truyentranh.model.Category;
import com.ali.truyentranh.tools.CallAPI;

public class CategoryFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    public static View viewTab;
    StableArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_category, container, false);

        this.viewTab = view;
        ListView listView = (ListView)view.findViewById(R.id.listCategory);
        adapter = new StableArrayAdapter(MainActivity.context, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Category item = (Category)adapter.getItem(position);
                Intent myIntent = new Intent(MainActivity.context, CategoryActivity.class);
                myIntent.putExtra("category", item.toString());
                MainActivity.context.startActivity(myIntent);
            }
        });

        return view;
    }

    private class StableArrayAdapter extends BaseAdapter {

        Category[] listCategory;
        Context context;

        public StableArrayAdapter(Context context, Category[] cate) {
            this.listCategory = cate;
            this.context = context;

            String api = "http://comicvn.net/truyentranh/apiv2/theloai";
            new CallAPICategory().execute(api);
        }

        @Override
        public int getCount() {
            if (this.listCategory == null) {
                return 0;
            }
            return this.listCategory.length;
        }

        @Override
        public Category getItem(int position) {
            if(this.listCategory == null || this.listCategory.length < position) {
                return null;
            }
            return this.listCategory[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView = (TextView) grid.findViewById(android.R.id.text1);
            textView.setText(this.listCategory[position].getName());
            return grid;
        }

        public class CallAPICategory extends CallAPI {
            @Override
            protected void onPostExecute(String result) {
                updateResult(result);
            }
        }

        private void updateResult(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Category[] listCategory = gson.fromJson(result, Category[].class);
                if (listCategory.length > 0) {
                    this.listCategory = listCategory;
                    notifyDataSetChanged();
                }
            }
        }

    }
}
