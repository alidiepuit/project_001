package com.ali.truyentranh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ali.truyentranh.R;
import com.ali.truyentranh.model.Tools;
import com.ali.truyentranh.model.User;
import com.ali.truyentranh.tools.CallAPI;

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
                TextView error = (TextView)findViewById(R.id.error);
                EditText username = (EditText)findViewById(R.id.etUserName);
                EditText password = (EditText)findViewById(R.id.etPass);
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(name.length() > 0 && pass.length() > 0) {
                    apiURL = apiURL + "?username=" + name + "&password=" + Tools.encrypt(pass);
                    new LoginAPI().execute(apiURL);
                } else {
                    error.setText("Kiểm tra lại thông tin đăng nhập");
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

    private class LoginAPI extends CallAPI {
        protected void onPostExecute(String result) {
            if(result != null && result.length() > 0) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final User user = gson.fromJson(result, User.class);
                if(user.message != null && !user.message.isEmpty()) {
                    TextView error = (TextView)findViewById(R.id.error);
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

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            finish();
        }
    }

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
