package comic.ali.com.comicv2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AliPro on 2/23/2015.
 */
public class Category extends Item {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    public String getId() {return id;}
    public String getName() {return name;}
}
