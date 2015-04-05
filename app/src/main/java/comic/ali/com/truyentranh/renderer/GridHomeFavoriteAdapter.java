package comic.ali.com.truyentranh.renderer;

import android.content.Context;

import comic.ali.com.truyentranh.model.Manga;
import comic.ali.com.truyentranh.model.User;

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
