package comic.ali.com.comicv2.tools;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

/**
 * Created by AliPro on 3/31/2015.
 */
public class Ads {
    // Do not call this function from the main thread. Otherwise,
    // an IllegalStateException will be thrown.
    public static void getIdThread(Context mContext) {

        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);

        } catch (IOException e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).

        } catch (GooglePlayServicesNotAvailableException e) {
            // Google Play services is not available entirely.
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        final String id = adInfo.getId();
        final boolean isLAT = adInfo.isLimitAdTrackingEnabled();
    }
}
