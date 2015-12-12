package jagsc.org.abc2016springclient;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

/**
 * Created by yuuki on 2015/12/12.
 */
public class WearPlayingActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_wear);
    }
}
