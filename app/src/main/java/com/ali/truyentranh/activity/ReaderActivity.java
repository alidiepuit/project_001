package com.ali.truyentranh.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ali.truyentranh.model.MyTarget;
import com.ali.truyentranh.model.Tools;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.ali.truyentranh.R;
import com.ali.truyentranh.model.Chapter;
import com.ali.truyentranh.renderer.FullScreenImageAdapter;
import com.ali.truyentranh.tools.Ads;
import com.ali.truyentranh.tools.Analytics;
import com.ali.truyentranh.tools.CallAPI;
import com.ali.truyentranh.viewmodel.ReaderViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import so.droidman.AsyncImageLoader;
import so.droidman.Utils;


public class ReaderActivity extends Activity implements AsyncImageLoader.onProgressUpdateListener {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/hinhtruyen";
    public static Context context = null;
    ReaderViewPager viewPager;
    int current_ord;
    FullScreenImageAdapter adapter;
    public static boolean startSwiping = true;
    public static boolean startSwipingRightToLeft = true;
    ProgressDialog pDialog;
    String key = "chapter";
    String listImage = "";
    ProgressBar progressBar;
    private final int SDK = Tools.SDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        this.context = this;

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progress_bar);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(drawable);

        Intent intent = getIntent();
        String idChapter = intent.getStringExtra("id_chapter");
        current_ord = Integer.valueOf(intent.getStringExtra("ord"));

        adapter = new FullScreenImageAdapter(ReaderActivity.this, null);
        viewPager = (ReaderViewPager) findViewById(R.id.view_pager_reader);
        viewPager.setAdapter(adapter);
        viewPager.setOnSwipeOutListener(new ReaderViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
                if(startSwiping) {
                    startSwiping = false;
                    ArrayList<Chapter> chapters = DetailActivity.listChapter;
                    Chapter firstChapter = chapters.get(chapters.size() - 1);
                    if (current_ord - 1 < Integer.valueOf(firstChapter.ord)) {
                        Toast.makeText(ReaderActivity.context, "Bạn đang đọc chap đầu tiên.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                    String idChapter = "";
                    for (Chapter ch : chapters) {
                        if (Integer.valueOf(ch.ord) <= current_ord - 1) {
                            idChapter = ch.id;
                            break;
                        }
                    }
                    current_ord -= 1;
                    String api = apiURL + "?id=" + idChapter;
                    new CallAPIChapter().execute(api);
                    */
                }
            }

            @Override
            public void onSwipeOutAtEnd() {
                if(startSwipingRightToLeft) {
                    startSwipingRightToLeft = false;
                    ArrayList<Chapter> chapters = DetailActivity.listChapter;
                    Chapter lastChapter = chapters.get(0);
                    if(current_ord+1 > Integer.valueOf(lastChapter.ord)) {
                        Toast.makeText(ReaderActivity.context, "Bạn đang đọc chap mới nhất.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                /*
                String idChapter = "";
                int minOrd = -1;
                for(Chapter ch:chapters) {
                    if(Integer.valueOf(ch.ord) >= current_ord+1 && (minOrd == -1 || minOrd > Integer.valueOf(ch.ord))) {
                        idChapter = ch.id;
                        minOrd = Integer.valueOf(ch.ord);
                    }
                    if(Integer.valueOf(ch.ord) < current_ord+1) {
                        break;
                    }
                }
                current_ord += 1;
                String api = apiURL + "?id=" + idChapter;
                new CallAPIChapter().execute(api);
                */
                }
            }
        });


        String api = apiURL + "?id=" + idChapter;
        this.key = Tools.md5(api);
        this.listImage = Tools.getData(this, "data", this.key);
        this.updateListImage(this.listImage);

        new CallAPIChapter().execute(api);

        //Get a Tracker (should auto-report)
        Tracker t = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("Read");
        t.send(new HitBuilders.AppViewBuilder().build());
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

    @Override
    public void doUpdateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    private class CallAPIChapter extends CallAPI {
        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(ReaderActivity.this, getString(R.string.dialog_title), getString(R.string.dialog_loading), true, false);
        }
        protected void onPostExecute(String result) {
            if (result != null && !result.isEmpty() && result.length() > 0 && listImage != result) {
                Tools.storeData(ReaderActivity.context, "data", key, result);
                updateListImage(result);
            }
            pDialog.dismiss();
        }
    }

    void updateListImage(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final String[] listImage = gson.fromJson(result, String[].class);

                ArrayList<String> list = new ArrayList<String>(Arrays.asList(listImage));
                int index = 0;
                if (list.size() > 1) {
                    Random randomGenerator = new Random();
                    index = randomGenerator.nextInt(list.size() - 1);
                }
                list.add(index, "ads");

                adapter.setData(list);
                adapter.notifyDataSetChanged();

//                initListImage(result);
            }
    }

    private void toggleProgressVisibility(boolean visible) {
        if (visible)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    void initListImage(String listImage) {
        toggleProgressVisibility(true);
        AsyncImageLoader loader = new AsyncImageLoader(
                new AsyncImageLoader.onImageLoaderListener() {

                    @Override
                    public void onImageLoaded(String ok,
                                              String response) {
                        if(ok.equals("OK")) {
                            toggleProgressVisibility(false);
                        }
                    }

                }, true, true,
                this);
        if (SDK >= 11)
            loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    listImage);
        else
            loader.execute(listImage);
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
}
