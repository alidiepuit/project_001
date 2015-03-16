package comic.ali.com.comicv2.renderer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Manga;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;

/**
 * Created by AliPro on 3/15/2015.
 */
public class GridHomeAdapter extends BaseAdapter {
    Context context;
    Manga[] listManga;
    public GridHomeAdapter(Context context, Manga[] list) {
        this.context = context;
        this.listManga = list;

        CallAPI callAPI = new CallAPI();
        String api = "http://comicvn.net/truyentranh/apiv2/truyenhot";
        callAPI.execute(api);
    }

    @Override
    public int getCount() {
        if (this.listManga == null) {
            return 0;
        }
        return this.listManga.length;
    }

    @Override
    public Object getItem(int position) {
        if(this.listManga == null || this.listManga.length < position) {
            return null;
        }
        return this.listManga[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(this.context);
            grid = inflater.inflate(R.layout.tv_show_row, null);
            TextView textView = (TextView) grid.findViewById(R.id.tv_title);
            ImageView imageView = (ImageView) grid.findViewById(R.id.iv_thumbnail);
            textView.setText(this.listManga[position].getTitle());
            Picasso.with(this.context).load(this.listManga[position].getPoster())
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(imageView);
        } else {
            grid = (View) convertView;
        }
        return grid;
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
            Manga[] listManga = gson.fromJson(result, Manga[].class);
            if (listManga.length > 0) {
                this.listManga = listManga;
                notifyDataSetChanged();
            }
        }
    }
}
