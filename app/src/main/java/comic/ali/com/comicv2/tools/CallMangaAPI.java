package comic.ali.com.comicv2.tools;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.activity.DetailActivity;
import comic.ali.com.comicv2.activity.TvShowsActivity;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Item;
import comic.ali.com.comicv2.renderer.ListChapterArrayAdapter;
import comic.ali.com.comicv2.renderer.TvShowAdapter;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;

/**
 * Created by AliPro on 3/8/2015.
 */
public class CallMangaAPI extends CallAPI{
    @Override
    protected void onPostExecute(String result) {
        if(result != null && result.length() > 0) {
        }
    }
}
