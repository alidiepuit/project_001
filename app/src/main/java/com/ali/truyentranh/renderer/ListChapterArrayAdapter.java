package com.ali.truyentranh.renderer;

/**
 * Created by AliPro on 2/20/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.ali.truyentranh.R;
import com.ali.truyentranh.model.Chapter;

public class ListChapterArrayAdapter extends ArrayAdapter<Chapter> {
    private final Context context;
    private final ArrayList<Chapter> values;

    public ListChapterArrayAdapter(Context context, ArrayList<Chapter> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    static class ViewHoder {
        public final TextView ord;
        public final TextView name;
        public ViewHoder(TextView ord, TextView name) {
            this.ord = ord;
            this.name = name;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.episode_row, null);
            TextView ord = (TextView) rowView.findViewById(R.id.tv_episode_number);
            TextView name = (TextView) rowView.findViewById(R.id.tv_episode_title);
            rowView.setTag(new ViewHoder(ord, name));
        }
        ViewHoder holder = (ViewHoder)rowView.getTag();
        Chapter chapter = this.values.get(position);
        holder.ord.setText(chapter.ord);
        holder.name.setText(chapter.name);
        if(chapter.checkVisited(this.context)) {
            holder.name.setTextColor(Color.BLUE);
        }
        return rowView;
    }
}
