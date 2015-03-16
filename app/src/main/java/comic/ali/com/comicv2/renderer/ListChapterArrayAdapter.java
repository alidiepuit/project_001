package comic.ali.com.comicv2.renderer;

/**
 * Created by AliPro on 2/20/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.model.Item;

public class ListChapterArrayAdapter extends ArrayAdapter<Chapter> {
    private final Context context;
    private final Chapter[] values;

    public ListChapterArrayAdapter(Context context, Chapter[] values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.episode_row, null);
        TextView ord = (TextView) convertView.findViewById(R.id.tv_episode_number);
        ord.setText(this.values[position].ord);
        TextView name = (TextView) convertView.findViewById(R.id.tv_episode_title);
        name.setText(this.values[position].name);
        return convertView;
    }
}
