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
public class WearOnemoreActivity extends WearableActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DataApi.DataListener{ //DataApi.DataListener  {
    private Button button_title;//titleへ戻るボタン
    private Button button_exit;//終了ボタン
    private String scene;//dataAPIのシーン情報のkey
    private String datapath;//データアクセスパス

    private GlobalVariables globalv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onemore_wear);//activity_onemore_wearを表示


        button_title=(Button)findViewById(R.id.btn_title);
        button_title.setOnClickListener(this);
        button_exit=(Button)findViewById(R.id.btn_to_exit);
        button_exit.setOnClickListener(this);

        globalv=(GlobalVariables) this.getApplication();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_title://btn_titleが押されたら
                Intent intent = new Intent(this, MainActivity.class);//MainActivityへ遷移
                startActivity(intent);
                break;
            case R.id.btn_to_exit://btn_to_exitが押されたらアプリケーション終了
                this.moveTaskToBack (true);
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
        if(scene.equals("scene:Title")) {
            Intent intent = new Intent(this, MainActivity.class);//MainActivityへ遷移
            startActivity(intent);
        }else if(scene.equals("scene:Exit")){
            this.moveTaskToBack(true);
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
