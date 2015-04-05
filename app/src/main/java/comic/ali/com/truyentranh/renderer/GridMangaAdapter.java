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

import java.util.ArrayList;
import java.util.Arrays;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.model.Manga;
import comic.ali.com.truyentranh.tools.CallAPI;

/**
 * Created by AliPro on 3/15/2015.
 */
public class GridMangaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Manga> listManga;
    public static Boolean loading = false;
    public GridMangaAdapter(Context context, Manga[] list, String api) {
        this.context = context;
        if(list != null && list.length > 0) {
            this.listManga = new ArrayList<Manga>(Arrays.asList(list));
        }
        if(api != null && !api.isEmpty()) {
            loading = true;
            new CallAPIGrid().execute(api);
        }
    }

    public Boolean loadingGrid() {
        return loading;
    }

    @Override
    public int getCount() {
        if (this.listManga == null) {
            return 0;
        }
        return this.listManga.size();
    }

    @Override
    public Object getItem(int position) {
        if(this.listManga == null || this.listManga.size() < position) {
            return null;
        }
        return this.listManga.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHoder {
        public final TextView textView;
        public final ImageView imageView;
        public ViewHoder(TextView text, ImageView image) {
            textView = text;
            imageView = image;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.tv_show_row, null);
            TextView textView = (TextView) rowView.findViewById(R.id.tv_title);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_thumbnail);
            rowView.setTag(new ViewHoder(textView, imageView));
        }
        ViewHoder holder = (ViewHoder)rowView.getTag();
        try {
            Manga data = this.listManga.get(position);
            holder.textView.setText(data.getTitle());
            Picasso.with(this.context).load(data.getPoster())
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(holder.imageView);
        }catch (Exception e) {
            //
        }
        return rowView;
    }

    public class CallAPIGrid extends CallAPI {
        @Override
        protected void onPostExecute(String result) {
            updateList(result);
        }
    } // end CallAPI

    public void updateList(String result) {
        if(result != null && result.length() > 0) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            Manga[] listManga = gson.fromJson(result, Manga[].class);
            if (listManga.length > 0) {
                this.listManga = new ArrayList<Manga>(Arrays.asList(listManga));
                notifyDataSetChanged();
            }
        }
    }

    public void addMoreData(String result) {
        if(result != null && result.length() > 0) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            Manga[] listManga = gson.fromJson(result, Manga[].class);
            if (listManga.length > 0) {
                this.listManga.addAll(Arrays.asList(listManga));
                notifyDataSetChanged();
            }
        }
    }
}
