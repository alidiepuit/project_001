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

        CallAPI callAPI = new CallAPI();
        String api = "http://comicvn.net/truyentranh/apiv2/truyenhot";
        callAPI.execute(api);
    }

    public class CallAPI extends AsyncTask<String, String, String> {
        String response;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            String apiURL = params[0];
            try {
                url = new URL(apiURL.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp;
                StringBuilder builder = new StringBuilder();
                while ((temp = bReader.readLine()) != null) {
                    builder.append(temp);
                }
                String res = builder.toString();

                this.response = res;
            } catch (Exception e) {
                Exception exp = e;
            } finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return this.response;
        }

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
