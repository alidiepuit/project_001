package comic.ali.com.comicv2.model;

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
}
