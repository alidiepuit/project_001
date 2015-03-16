package comic.ali.com.comicv2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by AliPro on 3/8/2015.
 */
public abstract class Item {
    @Override
    public String toString() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
