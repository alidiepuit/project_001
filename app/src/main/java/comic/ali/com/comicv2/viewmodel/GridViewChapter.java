package comic.ali.com.comicv2.viewmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by AliPro on 3/11/2015.
 */
public class GridViewChapter extends GridView {

    public GridViewChapter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewChapter(Context context) {
        super(context);
    }

    public GridViewChapter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
