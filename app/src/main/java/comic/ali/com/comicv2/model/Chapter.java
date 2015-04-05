package comic.ali.com.comicv2.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        editor.putBoolean(id, true);
        editor.commit();
    }

    public boolean checkVisited(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(id, false);
    }
}
