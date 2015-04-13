package com.ali.truyentranh.renderer;

import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.LinkedList;
import java.util.List;

import com.ali.truyentranh.tools.CallAPI;
import com.ali.truyentranh.viewmodel.TvShowViewModel;

/**
 * Created by AliPro on 3/10/2015.
 */
public class TvShowAdapter extends RendererAdapter<TvShowViewModel> {
    AdapteeCollection<TvShowViewModel> collection;
    public TvShowAdapter(LayoutInflater layoutInflater, RendererBuilder rendererBuilder, AdapteeCollection<TvShowViewModel> collection) {
        super(layoutInflater, rendererBuilder, collection);
        this.collection = collection;

        String api = "http://comicvn.net/truyentranh/apiv2/truyenhot";
        new CallAPITruyenHot().execute(api);
    }

    public class CallAPITruyenHot extends CallAPI {
        @Override
        protected void onPostExecute(String result) {
            updateList(result);
        }
    } // end CallAPI

    public void updateList(String result) {
        if(result != null && result.length() > 0) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            TvShowViewModel[] listManga = gson.fromJson(result, TvShowViewModel[].class);
            if (listManga.length > 0) {
                List<TvShowViewModel> tvShows = new LinkedList<TvShowViewModel>();
                for (TvShowViewModel manga : listManga) {
                    TvShowViewModel tvShow = new TvShowViewModel(manga.getId(), manga.getTitle(),
                            manga.getPoster(),
                            manga.getArt(),
                            manga.getDescription());
                    tvShows.add(tvShow);
                }
                this.collection.addAll(tvShows);
                notifyDataSetChanged();
            }
        }
    }
}
