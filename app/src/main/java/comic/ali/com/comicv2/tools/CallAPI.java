package comic.ali.com.comicv2.tools;

import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Item;
import comic.ali.com.comicv2.renderer.ListChapterArrayAdapter;

/**
 * Created by AliPro on 3/8/2015.
 */
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
} // end CallAPI
