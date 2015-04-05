package comic.ali.com.comicv2.renderer;

import android.os.AsyncTask;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import comic.ali.com.comicv2.activity.TvShowsActivity;
import comic.ali.com.comicv2.tools.CallAPI;
import comic.ali.com.comicv2.tools.CallMangaAPI;
import comic.ali.com.comicv2.viewmodel.TvShowCollectionViewModel;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;

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
