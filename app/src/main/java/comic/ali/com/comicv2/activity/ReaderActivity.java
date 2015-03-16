package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.renderer.FullScreenImageAdapter;


public class ReaderActivity extends Activity {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/hinhtruyen?id=";
    public static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        this.context = this;

        Intent intent = getIntent();
        String idChapter = intent.getStringExtra("id_chapter");
        apiURL = apiURL + idChapter;
        new CallAPI().execute(apiURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CallAPI extends AsyncTask<String, String, String[]> {
        String[] response;
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
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
//                String res = "{\"sys\":{\"country\":\"GB\",\"sunrise\":1381107633,\"sunset\":1381149604}}";
//                object = new JSONObject(res);

                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                this.response = gson.fromJson(res, String[].class);
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

        protected void onPostExecute(String[] result) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_reader);
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(result));
            FullScreenImageAdapter adapter = new FullScreenImageAdapter(ReaderActivity.this, list);
            viewPager.setAdapter(adapter);
        }
    } // end CallAPI
}
