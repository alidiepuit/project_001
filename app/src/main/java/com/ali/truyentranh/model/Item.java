package com.ali.truyentranh.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AliPro on 3/8/2015.
 */
public class Item {
    @SerializedName("result")
    public int result;

    @SerializedName("status")
    public int status;

    @Override
    public String toString() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
