package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.renderer.ListChapterArrayAdapter;
import comic.ali.com.comicv2.viewmodel.ExpandableTextView;
import comic.ali.com.comicv2.viewmodel.GridViewChapter;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;


public class DetailActivity extends Activity {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/truyenchap?id=";
    public static Context context = null;

    TextView header;
    TvShowViewModel detailManga;
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
            detailManga = gson.fromJson(value, TvShowViewModel.class);
            setTitle(detailManga.getTitle());
            ImageView imageDetail = (ImageView) findViewById(R.id.imageView);
            Picasso.with(context)
                    .load(detailManga.getPoster())
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(imageDetail);
            apiURL = apiURL + detailManga.getId();
            new CallAPI().execute(apiURL);
        }catch(Exception e) {

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                String userid = sharedPref.getString("userid", "");

                //not login
                if(userid.isEmpty()) {
                    Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                    DetailActivity.this.startActivity(intent);
                    return true;
                }

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
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet("http://comicvn.net/");
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NetworkOnMainThreadException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {
        String response;
        Activity activity;
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

        protected void onPostExecute(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final Chapter[] listChapter = gson.fromJson(result, Chapter[].class);
                if (listChapter.length > 0) {
                    GridViewChapter gridViewChapter = (GridViewChapter) findViewById(R.id.listChapter);

                    header = (TextView) getLayoutInflater().inflate(R.layout.episode_header, null);
                    header.setText("SEASON 1");

                    //description manga
                    ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.lorem_ipsum);
                    expandableTextView.setText(detailManga.getDescription());

                    final ListChapterArrayAdapter adapter = new ListChapterArrayAdapter(DetailActivity.context, listChapter);

                    ListChapterArrayAdapter chapterAdapter = new ListChapterArrayAdapter(DetailActivity.context,listChapter);
                    gridViewChapter.setNumColumns(1);
                    gridViewChapter.setAdapter(chapterAdapter);
                    gridViewChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            final Chapter item = (Chapter) adapterView.getItemAtPosition(position);
                            Intent myIntent = new Intent(DetailActivity.this, ReaderActivity.class);
                            myIntent.putExtra("id_chapter", item.id);
                            DetailActivity.this.startActivity(myIntent);
                        }
                    });
                }
            }
        }
    } // end CallAPI
}
