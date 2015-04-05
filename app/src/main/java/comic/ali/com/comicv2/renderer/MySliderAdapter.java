package comic.ali.com.comicv2.renderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import comic.ali.com.comicv2.R;
import comic.ali.com.comicv2.activity.DetailActivity;
import comic.ali.com.comicv2.activity.MainActivity;
import comic.ali.com.comicv2.activity.ReaderActivity;
import comic.ali.com.comicv2.activity.TvShowsActivity;
import comic.ali.com.comicv2.fragment.HomeFragment;
import comic.ali.com.comicv2.model.Chapter;
import comic.ali.com.comicv2.tools.CallAPI;
import comic.ali.com.comicv2.viewmodel.TvShowViewModel;

/**
 * Created by AliPro on 3/10/2015.
 */
public class MySliderAdapter{
    Context context;
    public MySliderAdapter(Context context) {
        this.context = context;

        String api = "http://comicvn.net/truyentranh/apiv2/truyenmoi?num=5";
        new CallAPITruyenMoi().execute(api);
    }

    public class CallAPITruyenMoi extends CallAPI {
        @Override
        protected void onPostExecute(String result) {
            updateList(result);
        }
    } // end CallAPI

    public void updateList(String result) {
        if(result != null && result.length() > 0) {
//            SliderLayout mDemoSlider = (SliderLayout) HomeFragment.viewTab.findViewById(R.id.slideHome);
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            TvShowViewModel[] listManga = gson.fromJson(result, TvShowViewModel[].class);
            if (listManga.length > 0) {
                for (TvShowViewModel manga : listManga) {
                    final String detail = manga.toString();
                    TextSliderView textSliderView = new TextSliderView(this.context);
                    // initialize a SliderLayout
                    textSliderView
                            .setScaleType(TextSliderView.ScaleType.CenterCrop)
                            .description(manga.getTitle())
                            .image(manga.getPoster())
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent myIntent = new Intent(MainActivity.context, DetailActivity.class);
                                    myIntent.putExtra("detail", detail);
                                    MainActivity.context.startActivity(myIntent);
                                }
                            });

//                    mDemoSlider.addSlider(textSliderView);
                }
//                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//                mDemoSlider.setDuration(4000);
            }
        }
    }
}
