package comic.ali.com.truyentranh.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.model.Manga;
import comic.ali.com.truyentranh.tools.CallAPI;

/**
 * Created by AliPro on 3/15/2015.
 */
public class GridHomeAdapter extends BaseAdapter {
    Context context;
    Manga[] listManga;
    public GridHomeAdapter(Context context, Manga[] list) {
        this.context = context;
        this.listManga = list;

        String api = "http://comicvn.net/truyentranh/apiv2/truyenhot";
        new CallAPITruyenHot().execute(api);
    }

    @Override
    public int getCount() {
        if (this.listManga == null) {
            return 0;
        }
        return this.listManga.length;
    }

    @Override
    public Object getItem(int position) {
        if(this.listManga == null || this.listManga.length < position) {
            return null;
        }
        return this.listManga[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(this.context);
            grid = inflater.inflate(R.layout.tv_show_row, null);
            TextView textView = (TextView) grid.findViewById(R.id.tv_title);
            ImageView imageView = (ImageView) grid.findViewById(R.id.iv_thumbnail);
            try {
                textView.setText(this.listManga[position].getTitle());
                Picasso.with(this.context).load(this.listManga[position].getPoster())
                        .placeholder(R.drawable.tv_show_placeholder)
                        .into(imageView);
            }catch (Exception e) {
                //
            }
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

    public class CallAPITruyenHot extends CallAPI {
        @Override
        protected void onPostExecute(String result) {
            updateList(result);
        }
    }

    public void updateList(String result) {
        if(result != null && result.length() > 0) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            Manga[] listManga = gson.fromJson(result, Manga[].class);
            if (listManga.length > 0) {
                this.listManga = listManga;
                notifyDataSetChanged();
            }
        }
    }
}
