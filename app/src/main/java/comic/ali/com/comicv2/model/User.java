package comic.ali.com.comicv2.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AliPro on 3/14/2015.
 */
public class User {
    @SerializedName("email")
    public String username;

    @SerializedName("userid")
    public String userid;

    @SerializedName("success")
    public int logined;

    @SerializedName("message")
    public String message;

    private Context context;
    public User(Context context) {
        this.context = context;
    }

    public boolean isLogin() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String userId = sharedPref.getString("userid", "");
        this.userid = userId;
        return !userId.isEmpty();
    }

    public void logout() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public String getUserId() {
        return this.userid;
    }
}
