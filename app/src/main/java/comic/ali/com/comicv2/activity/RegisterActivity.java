package comic.ali.com.comicv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Tools;
import comic.ali.com.comicv2.model.User;

public class RegisterActivity extends Activity {
    public final String apiURL = "http://comicvn.net/truyentranh/apiv2/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button but = (Button) findViewById(R.id.btnRegister);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText)findViewById(R.id.etUserName);
                EditText password = (EditText)findViewById(R.id.etPass);
                EditText passwordRepeat = (EditText)findViewById(R.id.etPassRepeat);
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String passRepeat = passwordRepeat.getText().toString();
                if(name.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") &&
                        pass.length() >= 6 && pass.length() < 50 &&
                        pass.equals(passRepeat)) {
                    String api = apiURL + "?username=" + name + "&password=" + Tools.encrypt(pass);
                    new CallAPI().execute(api);
                } else {
                    TextView error = (TextView)findViewById(R.id.errorRegister);
                    error.setText("Kiểm tra lại thông tin đăng nhập");
                }
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
                if(user.message != null && !user.message.isEmpty()) {
                    TextView error = (TextView)findViewById(R.id.errorRegister);
                    error.setText(user.message);
                    return;
                }
                if(user.logined == 1) {
                    SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", user.username);
                    editor.putInt("logined", user.logined);
                    editor.putString("userid", user.userid);
                    editor.commit();
                    Toast.makeText(getBaseContext(), "Chào mừng " + user.username, Toast.LENGTH_LONG);
                    finish();
                }
            }
        }
    } // end CallAPI

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
