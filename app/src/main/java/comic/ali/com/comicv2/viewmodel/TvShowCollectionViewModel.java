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
package comic.ali.com.comicv2.viewmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrogomez.renderers.AdapteeCollection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Item;
import comic.ali.com.comicv2.tools.CallMangaAPI;

/**
 * TvShowCollectionViewModel implementation used to contains all the tv shows information. This
 * implementation is based on a LinkedList with hardcoded data.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TvShowCollectionViewModel implements AdapteeCollection<TvShowViewModel> {

    List<TvShowViewModel> tvShows = new LinkedList<TvShowViewModel>();

    @Inject
    public TvShowCollectionViewModel() {
    }

    /**
     * Return the number of tv shows inside the collection.
     */
    @Override
    public int size() {
        return tvShows.size();
    }

    /**
     * Return a TvShowViewModel obtained by position.
     */
    @Override
    public TvShowViewModel get(int i) {
        return tvShows.get(i);
    }

    /**
     * Add a TvShowViewModel to the collection.
     */
    @Override
    public void add(TvShowViewModel tvShowViewModel) {
        tvShows.add(tvShowViewModel);
    }

    /**
     * Remove a TvShowViewModel from the collection.
     */
    @Override
    public void remove(TvShowViewModel tvShowViewModel) {
        tvShows.remove(tvShowViewModel);
    }

    /**
     * Add a collection of TvShowViewModel to the collection.
     */
    @Override
    public void addAll(Collection<TvShowViewModel> tvShowViewModels) {
        tvShows.addAll(tvShowViewModels);
    }

    /**
     * Remove a collection of TvShowViewModel to the collection.
     */
    @Override
    public void removeAll(Collection<TvShowViewModel> tvShowViewModels) {
        tvShowViewModels.removeAll(tvShowViewModels);
    }
}
