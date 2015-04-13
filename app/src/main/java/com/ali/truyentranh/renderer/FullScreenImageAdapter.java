package com.ali.truyentranh.renderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ali.truyentranh.model.MyTarget;
import com.ali.truyentranh.model.Tools;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.ali.truyentranh.R;
import com.ali.truyentranh.viewmodel.TouchImageView;
import com.squareup.picasso.Target;

/**
 * Created by AliPro on 2/25/2015.
 */
public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private MyTarget target = new MyTarget();

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    public void setData(ArrayList<String> list) {
        this._imagePaths = list;
    }

    @Override
    public int getCount() {
        if(this._imagePaths == null || this._imagePaths.size() <= 0) {
            return 0;
        }
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    static class ViewHolder {
        TouchImageView image;
        ProgressBar progressBar;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
        if(_imagePaths.get(position) == "ads") {
            AdView mAdView = (AdView)viewLayout.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
            ((ViewPager) container).addView(viewLayout);
            return viewLayout;
        }

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);

        String path = target.getPath(Tools.md5(_imagePaths.get(position)));
        if (path.isEmpty() || path.length() <= 0) {
            Picasso.with(_activity)
                    .load(_imagePaths.get(position))
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(imgDisplay);

            target.setName(_imagePaths.get(position));
//            Picasso.with(_activity)
//                    .load(_imagePaths.get(position))
//                    .into(target);
        } else {
            Picasso.with(_activity)
                    .load(new File(path))
                    .placeholder(R.drawable.tv_show_placeholder)
                    .into(imgDisplay);
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
