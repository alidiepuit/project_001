package com.ali.truyentranh.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AliPro on 2/23/2015.
 */
public class Chapter extends Item {
    @SerializedName("id_chapter")
    public String id;

    @SerializedName("chapter_name")
    public String name;

    @SerializedName("chapter_ord")
    public String ord;

    public void setVisited(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(id + "_read", true);
        editor.commit();
    }

    public boolean checkVisited(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(id + "_read", false);
    }

    public void setDownloaded(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(id + "_downloaded", true);
        editor.commit();
    }

    public boolean checkDownloaded(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(id + "_downloaded", false);
    }
}
