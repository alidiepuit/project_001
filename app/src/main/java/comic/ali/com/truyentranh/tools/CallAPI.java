package comic.ali.com.truyentranh.tools;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            response = builder.toString();
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
        return response;
    }
}
