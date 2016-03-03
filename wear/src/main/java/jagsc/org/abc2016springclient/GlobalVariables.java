package jagsc.org.abc2016springclient;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**

 * Created by prprhyt on 2016/02/14.
 */
public class GlobalVariables extends Application {
    GoogleApiClient mGoogleApiClient;
    final static String DATA_PATH = "/abc2016cdata";
}

