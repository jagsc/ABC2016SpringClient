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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.NodeApi;
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
    private Button btn_start;
    private Button button_result;//result画面への遷移のボタン
    private String scene;//dataAPIのシーン情報のkey
    private boolean ready;//準備完了状態
    private String datapath;

    private GlobalVariables globalv;

    private int state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_wear);//起動時にactivity_title_wearを表示する
        setAmbientEnabled();

        globalv=(GlobalVariables) this.getApplication();

        WearBluetoothTask wbttask = new WearBluetoothTask();//WearBluetoothTaskのコンストラクタ呼び出し
        wbttask.strstate = wbttask.getBondState(state);//strstateに接続中かエラーかの文字が入る

        //Bluetoothの接続ができているか判定し、readyの状態を変更する。
        if(wbttask.equals("接続中")){
            ready = true;
            btn_start.setVisibility(View.VISIBLE);
        }else if(wbttask.equals("エラー")){
            ready = false;
            btn_start.setVisibility(View.INVISIBLE);
            mTextView.setText("Bluetoothの設定を行ってから再度実行してください");
        }

        globalv.mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
        //mGoogleApiClient.connect();//接続

        /*mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);
        */
        btn_start=(Button)findViewById(R.id.btn_to_start);
        btn_start.setOnClickListener(this);

        /*button_result=(Button)findViewById(R.id.btn_to_result);//デバッグ用
        button_result.setOnClickListener(this);//デバッグ用
        */
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
            /*case R.id.btn_to_result://btn_to_resultボタンが押された(デバッグ用)
                Intent intent = new Intent(this, WearResultActivity.class);//WearResultActivityに遷移
                //intent.putExtra("Connection", (Serializable) mGoogleApiClient);//データ確立情報が次のactivityに送られる？
                startActivity(intent);
                break;
            */
            case R.id.btn_to_start://readyが真であるという情報をサーバへ送信する
                PutDataMapRequest dataMap = PutDataMapRequest.create(datapath);
                dataMap.getDataMap().putBoolean("ready",true );//readyというkeyでtrueという値が送られる
                PutDataRequest request = dataMap.asPutDataRequest();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                        .putDataItem(globalv.mGoogleApiClient, request);
                pendingResult.cancel();//送れなかったら動作をキャンセル？

                Intent intent = new Intent(this, WearPlayingActivity.class);//btn_to_startがクリックされたらWearPlayingActivityへ遷移
                startActivity(intent);
                break;
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
        DataItemBuffer itemBuffer = Wearable.DataApi.getDataItems(globalv.mGoogleApiClient).await();
        for(DataItem item : itemBuffer) {
            if(datapath.equals(item.getUri().getPath())) {
                DataMap map = DataMap.fromByteArray(item.getData());
                scene = map.getString("scene_name");//sceneにscene_nameという名で関連付けられたデータが入る?
            }
        }

        switch(scene){
            case "scene:Playing":
                Intent intentplay = new Intent(this, WearPlayingActivity.class);//WearPlayingActivityへ遷移
                startActivity(intentplay);
            case  "scene:Result":
                Intent intentres = new Intent(this, WearResultActivity.class);//WearResultActivityへ遷移
                startActivity(intentres);
            case "scene:Onemore":
                Intent intentone = new Intent(this, WearOnemoreActivity.class);//WearOnemoreActivityへ遷移
                startActivity(intentone);
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