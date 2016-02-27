package jagsc.org.abc2016springclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableDialogActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.lang.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by yuuki on 2015/12/12.
 */
public class WearPlayingActivity extends WearableActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DataApi.DataListener{ //DataApi.DataListener  {

    //private GoogleApiClient mGoogleApiClient;
    private String scene;//dataAPIのシーン情報のkey
    private String datapath;//データアクセスパス
    private Boolean Vibe=false;//バイブするかどうかの真偽を入れる変数
    private Vibrator vib;//バイブさせる変数

    private GlobalVariables globalv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_wear);//activity_playing_wearを表示


        globalv=(GlobalVariables) this.getApplication();
        //mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
        //mGoogleApiClient.connect();//接続

        //PutDataRequest dataMapRequest = PutDataRequest.create(datapath);


    }

    @Override
    public void onClick(View v){//デバッグ用
        /*switch (v.getId()){
            case R.id.btn_vibe://btn_vibeが押されたら
                vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);//バイブさせるためのサービスをvibに入れる
                vib.vibrate(50);//50ミリ秒バイブさせる
                break;
        }
        */
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
    public void onDataChanged(DataEventBuffer dataEventBuffer) {//handheld側からのデータの受け取り部
        DataItemBuffer itemBuffer = Wearable.DataApi.getDataItems(globalv.mGoogleApiClient).await();
        for(DataItem item : itemBuffer) {
            if(datapath.equals(item.getUri().getPath())) {
                DataMap map = DataMap.fromByteArray(item.getData());
                scene = map.getString("scene_name");//sceneにscene_nameという名で関連付けられたデータが入る?
                Vibe = map.getBoolean("Vibe");//Vibeにtrueかfalseどちらかの値が入る
            }
        }
        if(Vibe==true){
            vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);//バイブさせるためのサービスをvibに入れる
            vib.vibrate(50);//50ミリ秒バイブさせる
            Vibe=false;
        }
        switch(scene){
            case  "scene:Title":
                Intent intenttit = new Intent(this, MainActivity.class);//MainActivityへ遷移
                startActivity(intenttit);
            case "scene:Result":
                Intent intentres = new Intent(this, WearResultActivity.class);//WearResultActivityへ遷移
                startActivity(intentres);
            case "scene:Onemore":
                Intent intentone = new Intent(this, WearOnemoreActivity.class);//WearOnemoreActivityへ遷移
                startActivity(intentone);
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