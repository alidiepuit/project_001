package comic.ali.com.comicv2.renderer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Manga;
import comic.ali.com.comicv2.model.User;
import comic.ali.com.comicv2.tools.CallAPI;

/**
 * Created by AliPro on 3/15/2015.
 */
public class GridHomeFavoriteAdapter extends GridMangaAdapter {
    public GridHomeFavoriteAdapter(Context context, Manga[] list) {
        super(context, list, null);
        User user = new User(context);
        if (!user.isLogin()) {
            return;
        }
        String api = "http://comicvn.net/truyentranh/apiv2/favorite?userId=" + user.getUserId();
        new CallAPIGrid().execute(api);
    }
}
