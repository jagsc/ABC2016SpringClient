package jagsc.org.abc2016springclient;


import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DataApi.DataListener,MessageApi.MessageListener{

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);

    //private GoogleApiClient mGoogleApiClient;
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    private Button button_result;//result画面への遷移のボタン
    private String scene;//dataAPIのシーン情報のkey
    private boolean ready;//準備完了状態

    private GlobalVariables globalv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_wear);//起動時にactivity_title_wearを表示する
        setAmbientEnabled();

        globalv=(GlobalVariables) this.getApplication();

        globalv.mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
        //mGoogleApiClient.connect();//接続

        /*mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);
        */
        button_result=(Button)findViewById(R.id.btn_to_result);//デバッグ用
        button_result.setOnClickListener(this);//デバッグ用
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        globalv.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(globalv.mGoogleApiClient != null && globalv.mGoogleApiClient.isConnected()){
            globalv.mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void updateDisplay() {
    /*    if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    */}
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_to_result://btn_to_resultボタンが押された
                Intent intent = new Intent(this, WearResultActivity.class);//WearResultActivityに遷移
                //intent.putExtra("Connection", (Serializable) mGoogleApiClient);//データ確立情報が次のactivityに送られる？
                startActivity(intent);
                break;
            case R.id.btn_to_start://readyを真にする

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("TAG", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG", "onConnectionSuspended");
    }



    public void onDataChanged(DataEventBuffer dataEventBuffer) {//dataAPIが更新されたら自動で呼び出される

        if (scene.equals("scene:Onemore")) {
            Intent intent = new Intent(this, WearOnemoreActivity.class);//WearOnemoreActivityへ遷移
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionFailed");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
}
