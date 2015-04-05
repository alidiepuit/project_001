package comic.ali.com.truyentranh.tools;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import comic.ali.com.truyentranh.R;

/**
 * Created by AliPro on 3/31/2015.
 */
public class Analytics extends Application{
    private static final String PROPERTY_ID = "UA-32956292-3";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId)
    {
        if (!mTrackers.containsKey(trackerId))
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if( trackerId == TrackerName.GLOBAL_TRACKER )
            {
                mTrackers.put(trackerId, analytics.newTracker(R.xml.global_tracker));
            } else {
                mTrackers.put(trackerId, analytics.newTracker(R.xml.analytics));
            }
        }
        return mTrackers.get(trackerId);
    }

}
