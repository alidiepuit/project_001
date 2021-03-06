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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ali.truyentranh.R;
import com.ali.truyentranh.model.Tools;
import com.ali.truyentranh.model.User;
import com.ali.truyentranh.tools.CallAPI;

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
                    new RegisterAPI().execute(api);
                } else {
                    TextView error = (TextView)findViewById(R.id.errorRegister);
                    error.setText("Kiểm tra lại thông tin đăng nhập");
                }
            }
        });
    }

    private class RegisterAPI extends CallAPI {
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
                    Toast.makeText(getBaseContext(), "Chào mừng " + user.username, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
