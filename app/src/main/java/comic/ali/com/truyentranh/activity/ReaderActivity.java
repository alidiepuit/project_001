package comic.ali.com.truyentranh.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.model.Chapter;
import comic.ali.com.truyentranh.renderer.FullScreenImageAdapter;
import comic.ali.com.truyentranh.tools.Ads;
import comic.ali.com.truyentranh.tools.Analytics;
import comic.ali.com.truyentranh.tools.CallAPI;
import comic.ali.com.truyentranh.viewmodel.ReaderViewPager;


public class ReaderActivity extends Activity {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/hinhtruyen";
    public static Context context = null;
    ReaderViewPager viewPager;
    int current_ord;
    FullScreenImageAdapter adapter;
    public static boolean startSwiping = true;
    public static boolean startSwipingRightToLeft = true;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        this.context = this;

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
        new CallAPIChapter().execute(api);

        new AdsCall().execute();


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

    private class CallAPIChapter extends CallAPI {
        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(ReaderActivity.this, getString(R.string.dialog_title), getString(R.string.dialog_loading), true, false);
        }
        protected void onPostExecute(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                String[] listImage = gson.fromJson(result, String[].class);

                ArrayList<String> list = new ArrayList<String>(Arrays.asList(listImage));
                int index = 0;
                if(list.size() > 1) {
                    Random randomGenerator = new Random();
                    index = randomGenerator.nextInt(list.size() - 1);
                }
                list.add(index, "ads");
                adapter.setData(list);
                adapter.notifyDataSetChanged();
            }
            pDialog.dismiss();
        }
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

    private class AdsCall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Ads.getIdThread(ReaderActivity.context);
            return null;
        }
    }
}
