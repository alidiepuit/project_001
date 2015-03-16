package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Tools;
import comic.ali.com.comicv2.model.User;
import comic.ali.com.comicv2.renderer.ListChapterArrayAdapter;
import comic.ali.com.comicv2.viewmodel.ExpandableTextView;
import comic.ali.com.comicv2.viewmodel.GridViewChapter;

public class LoginActivity extends Activity {
    public String apiURL = "http://comicvn.net/truyentranh/apiv2/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button but = (Button) findViewById(R.id.btnSingIn);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText)findViewById(R.id.etUserName);
                EditText password = (EditText)findViewById(R.id.etPass);
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(name.length() > 0 && pass.length() > 0) {
                    apiURL = apiURL + "?username=" + name + "&password=" + Tools.md5(pass);
                    new CallAPI().execute(apiURL);
                }
            }
        });

        Button butRegister = (Button) findViewById(R.id.btnRegister);
        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                final User user = gson.fromJson(result, User.class);
                if(user.logined == 1) {
                    SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", user.username);
                    editor.putInt("logined", user.logined);
                    editor.putString("userid", user.userid);
                    editor.commit();

//                    Intent intent = new Intent(LoginActivity.this, TvShowsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    finish();
                }
            }
        }
    } // end CallAPI

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
