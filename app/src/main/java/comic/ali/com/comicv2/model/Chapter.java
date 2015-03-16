package comic.ali.com.comicv2.model;

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
}
