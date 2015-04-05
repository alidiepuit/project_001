package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Item;
import comic.ali.com.comicv2.model.Manga;
import comic.ali.com.comicv2.model.User;
import comic.ali.com.comicv2.renderer.ListChapterArrayAdapter;
import comic.ali.com.comicv2.tools.Analytics;
import comic.ali.com.comicv2.tools.CallAPI;
import comic.ali.com.comicv2.viewmodel.ExpandableTextView;
import comic.ali.com.comicv2.viewmodel.GridViewChapter;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;


public class DetailActivity extends Activity {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/truyenchap?id=";
    public static Context context = null;

    TextView header;
    Manga detailManga;
    public static ArrayList<Chapter> listChapter;
    ProgressDialog pDialog;
    public static Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.context=this;

        Intent intent = getIntent();
        String value = intent.getStringExtra("detail");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            detailManga = gson.fromJson(value, Manga.class);
            setTitle(detailManga.getTitle());
            ImageView imageDetail = (ImageView) findViewById(R.id.imageView);
            Picasso.with(context)
                    .load(detailManga.getPoster())
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(imageDetail);
            apiURL = apiURL + detailManga.getId();
            new CallAPIGetChapter().execute(apiURL);
        }catch(Exception e) {

        }

        User user = new User(this);

        if(user.isLogin()) {
            String api = "http://comicvn.net/truyentranh/apiv2/favorite?check=1&mangaId=" + detailManga.getId() + "&userId=" + user.getUserId();
            new CallAPICheckFavorite().execute(api);
        }

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

    public class CallAPICheckFavorite extends CallAPI {
        @Override
        protected void onPostExecute(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final Item data = gson.fromJson(result, Item.class);
                //check title: UnFavorite
                MenuItem item = DetailActivity.menu.findItem(R.id.action_favorite);
                if (data.status == 0) {
                    //set title: Favorite
                    item.setTitle(R.string.action_favorite);
                    item.setIcon(R.drawable.ic_action_favorite);
                } else {
                    //set title: UnFavorite
                    item.setTitle(R.string.action_favorite_active);
                    item.setIcon(R.drawable.ic_action_favorite_active);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                String userId = sharedPref.getString("userid", "");

                //not login
                if(userId.isEmpty()) {
                    Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                    DetailActivity.this.startActivity(intent);
                    return true;
                }

                String api = "http://comicvn.net/truyentranh/apiv2/favorite?mangaId=" + detailManga.getId() + "&userId=" + userId;
                new CallAPI().execute(api);

                //check title: UnFavorite
                if (item.getTitle() == getString(R.string.action_favorite_active)) {
                    //set title: Favorite
                    item.setTitle(R.string.action_favorite);
                    item.setIcon(R.drawable.ic_action_favorite);
                } else {
                    //set title: UnFavorite
                    item.setTitle(R.string.action_favorite_active);
                    item.setIcon(R.drawable.ic_action_favorite_active);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CallAPIGetChapter extends CallAPI {
        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(DetailActivity.this, getString(R.string.dialog_title), getString(R.string.dialog_loading), true, false);
        }
        protected void onPostExecute(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final Chapter[] chapters = gson.fromJson(result, Chapter[].class);
                listChapter = new ArrayList<Chapter>(Arrays.asList(chapters));
                if (listChapter.size() > 0) {
                    GridViewChapter gridViewChapter = (GridViewChapter) findViewById(R.id.listChapter);

                    //description manga
                    ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.lorem_ipsum);
                    expandableTextView.setText(detailManga.getDescription());

                    final ListChapterArrayAdapter chapterAdapter = new ListChapterArrayAdapter(DetailActivity.context,listChapter);
                    gridViewChapter.setNumColumns(1);
                    gridViewChapter.setAdapter(chapterAdapter);
                    gridViewChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            final Chapter item = (Chapter) adapterView.getItemAtPosition(position);
                            Intent myIntent = new Intent(DetailActivity.this, ReaderActivity.class);
                            myIntent.putExtra("id_chapter", item.id);
                            myIntent.putExtra("ord", item.ord);

                            //visited
                            item.setVisited(DetailActivity.context);
                            TextView name = (TextView)view.findViewById(R.id.tv_episode_title);
                            name.setTextColor(Color.BLUE);

                            DetailActivity.this.startActivity(myIntent);
                        }
                    });
                }
            }
            pDialog.dismiss();
        }
    }
}
