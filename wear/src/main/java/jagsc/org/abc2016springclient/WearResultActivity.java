package jagsc.org.abc2016springclient;

import android.os.AsyncTask;
import android.os.Bundle;
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
public class WearResultActivity extends WearableActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DataApi.DataListener{ //DataApi.DataListener  {

    //private GoogleApiClient mGoogleApiClient;
    private Button button_onemore;//onmore画面への遷移のボタン
    private Button btn_win;
    private Button btn_lose;
    private String resultwl="win";//送られてきた勝敗結果が入る
    private String scene;//dataAPIのシーン情報のkey
    private String datapath;//データアクセスパス

    private GlobalVariables globalv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_wear);//activity_result_wearを表示


        button_onemore = (Button) findViewById(R.id.btn_to_onemore);
        button_onemore.setOnClickListener(this);

        globalv=(GlobalVariables) this.getApplication();
        //mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
        //mGoogleApiClient.connect();//接続

        //PutDataRequest dataMapRequest = PutDataRequest.create(datapath);



        btn_win = (Button) findViewById(R.id.btn_win);
        btn_lose = (Button) findViewById(R.id.btn_lose);
        if (resultwl.equals("win")==true) {//resultwlの中身がwinなら
            btn_win.setVisibility(View.VISIBLE);//btnwinのvisiblityをvisibleに
            btn_lose.setVisibility(View.INVISIBLE);//btnloseのvisiblityをinvisibleに
        } else if (resultwl.equals("lose")==true) {//resultの中身がloseなら
            btn_lose.setVisibility(View.VISIBLE);//btnloseのvisiblityをvisibleに
            btn_win.setVisibility(View.INVISIBLE);//btnwinのvisiblityをinvisibleに
        }
    }

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
            }
        }
        switch(scene){
            case  "scene:Title":
                Intent intenttit = new Intent(this, MainActivity.class);//MainActivityへ遷移
                startActivity(intenttit);
            case "scene:Playing":
                Intent intentplay = new Intent(this, WearPlayingActivity.class);//WearPlayingActivityへ遷移
                startActivity(intentplay);
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