package comic.ali.com.truyentranh.renderer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comic.ali.com.truyentranh.R;
import comic.ali.com.truyentranh.viewmodel.TouchImageView;

/**
 * Created by AliPro on 2/25/2015.
 */
public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

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
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            ((ViewPager) container).addView(viewLayout);
            return viewLayout;
        }

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);

        Picasso.with(_activity)
                .load(_imagePaths.get(position))
                .placeholder(R.drawable.tv_show_placeholder)
                .into(imgDisplay);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
