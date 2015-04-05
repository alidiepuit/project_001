package comic.ali.com.comicv2.viewmodel;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import comic.ali.com.comicv2.activity.ReaderActivity;

/**
 * Created by AliPro on 3/29/2015.
 */
public class ReaderViewPager extends ViewPager {
    boolean swiping = false;
    float mStartDragX;
    OnSwipeOutListener mListener;

    public ReaderViewPager(Context context) {
        super(context);
    }

    public ReaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentItem() == 0) {
                    mListener.onSwipeOutAtStart();
                } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
                    mListener.onSwipeOutAtEnd();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ReaderActivity.startSwiping = true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnSwipeOutListener {
        public void onSwipeOutAtStart();
        public void onSwipeOutAtEnd();
    }
}
