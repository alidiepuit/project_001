package comic.ali.com.truyentranh.renderer;

import android.content.Context;
import android.content.Intent;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import comic.ali.com.truyentranh.activity.DetailActivity;
import comic.ali.com.truyentranh.activity.MainActivity;
import comic.ali.com.truyentranh.tools.CallAPI;
import comic.ali.com.truyentranh.viewmodel.TvShowViewModel;

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
