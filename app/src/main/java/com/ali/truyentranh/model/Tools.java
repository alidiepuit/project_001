package com.ali.truyentranh.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.ali.truyentranh.R;
import com.ali.truyentranh.activity.DetailActivity;
import com.ali.truyentranh.activity.ReaderActivity;
import com.ali.truyentranh.tools.CallAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import so.droidman.AsyncImageLoader;

/**
 * Created by AliPro on 3/15/2015.
 */
public class Tools {
    private static final String publickey = "gHEutds==DHtehhapdd";
    public static String folder = "truyentranhonline";
    public static final int SDK = Build.VERSION.SDK_INT;

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void storeData(Context context, String tag, String key, String data) {
        SharedPreferences sharedPref = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static String getData(Context context, String tag, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String data = sharedPref.getString(key, "");
        return data;
    }

    public static final String encrypt(final String s) {
        byte[] data = null;
        data = s.getBytes();
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        data = (base64 + publickey).getBytes();
        String res = Base64.encodeToString(data, Base64.NO_WRAP);
        return res;
    }

    public static void storeImage(final String url) {
        String name = Tools.md5(url);
        final File file = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + folder + File.separator + name + ".jpg");
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        if(!file.exists() || file_size <= 0) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        file.createNewFile();
                        URL myFileUrl = new URL(url);
                        Bitmap bmImg;
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl
                                .openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bmImg = BitmapFactory.decodeStream(is);
                        FileOutputStream ostream = new FileOutputStream(file);
                        bmImg.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void downloadChapter(Chapter chapter) {
        String apiURL = "http://comicvn.net/truyentranh/apiv2/hinhtruyen";
        String api = apiURL + "?id=" + chapter.id;
        new AsyncTask<String, String, String>() {
            String response = "";
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

            protected void onPostExecute(String result) {
                if (result != null && !result.isEmpty() && result.length() > 0) {
                    AsyncImageLoader loader = new AsyncImageLoader(
                            new AsyncImageLoader.onImageLoaderListener() {

                                @Override
                                public void onImageLoaded(String ok,
                                                          String response) {
                                    if(ok.equals("OK")) {
                                        Toast.makeText(DetailActivity.context, "Completed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }, true, true);
                    if (SDK >= 11)
                        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                result);
                    else
                        loader.execute(result);
                }
            }
        }.execute(api);
    }
}
