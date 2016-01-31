package jagsc.org.abc2016springclient;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import java.lang.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by yuuki on 2015/12/12.
 */
public class WearResultActivity extends WearableActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private Button button_onemore;//onmore画面への遷移のボタン
    private String resultwl="win";//送られてきた勝敗結果が入る


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_wear);//activity_result_wearを表示

         mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
         button_onemore=(Button)findViewById(R.id.btn_to_onemore);
         button_onemore.setOnClickListener(this);

    }
    Button btnwin = (Button) findViewById(R.id.btn_win);
    Button btnlose = (Button) findViewById(R.id.btn_lose);
    //勝敗結果が送られてきてそれがresultに入る
/*   if(resultwl.equals("win")){//resultwlの中身がwinなら
        btnwin.setVisibility(visible)//btnwinのvisiblityをvisibleに
        btnlose.setVisibility(invisible);//btnloseのvisiblityをinvisibleに
    }else if(resultwl.equals("lose")) {//resultの中身がloseなら
        btnlose.setVisibility(visible);//btnloseのvisiblityをvisibleに
        btnwin.setVisibility(invisible)//btnwinのvisiblityをinvisibleに
    }
*/
//↑ここの行でequalsが使えなく、更にvisiblityの変更もエラーになる



    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_to_onemore://btn_to_onemoreが押されたら
                Intent intent = new Intent(this, WearOnemoreActivity.class);//WearOnemoreActivityへ遷移
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
