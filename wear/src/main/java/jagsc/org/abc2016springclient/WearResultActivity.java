package jagsc.org.abc2016springclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableDialogActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.lang.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
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
    //private Button button_onemore;//onmore画面への遷移のボタン
    private Button btn_win;
    private Button btn_lose;
    private Button btn_exit;
    private String resultwl;//送られてきた勝敗結果が入る
    private TextView text_thank;
    private String scene;//dataAPIのシーン情報のkey
    private GlobalVariables globalv;
    private AlertDialog.Builder alertdialog;




     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_result_wear);//activity_result_wearを表示


        /*button_onemore = (Button) findViewById(R.id.btn_to_onemore);
        button_onemore.setOnClickListener(this);
        */
         globalv = (GlobalVariables) this.getApplication();

         PutDataRequest dataMapRequest = PutDataRequest.create(globalv.DATA_PATH);


         btn_win = (Button) findViewById(R.id.btn_win);
         btn_win.hasOnClickListeners();
         btn_lose = (Button) findViewById(R.id.btn_lose);
         btn_lose.hasOnClickListeners();
         btn_exit = (Button) findViewById(R.id.btn_win);
         btn_exit.hasOnClickListeners();
         text_thank = (TextView) findViewById(R.id.textView_thank);
        /*if (resultwl.equals("win")==true) {//resultwlの中身がwinなら
            btn_win.setVisibility(View.VISIBLE);//btnwinのvisiblityをvisibleに
            btn_lose.setVisibility(View.INVISIBLE);//btnloseのvisiblityをinvisibleに
        } else if (resultwl.equals("lose")==true) {//resultの中身がloseなら
            btn_lose.setVisibility(View.VISIBLE);//btnloseのvisiblityをvisibleに
            btn_win.setVisibility(View.INVISIBLE);//btnwinのvisiblityをinvisibleに
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_win:
                btn_win.setVisibility(View.INVISIBLE);//btnwinのvisiblityをinvisibleに
                btn_lose.setVisibility(View.INVISIBLE);//btnloseのvisiblityをinvisibleに
                text_thank.setVisibility(View.VISIBLE);//ありがとうございましたを表示
                break;
            case R.id.btn_lose:
                btn_win.setVisibility(View.INVISIBLE);//btnwinのvisiblityをinvisibleに
                btn_lose.setVisibility(View.INVISIBLE);//btnloseのvisiblityをinvisibleに
                text_thank.setVisibility(View.VISIBLE);//ありがとうございましたを表示
                break;
            case R.id.btn_exit_res:
                alertdialog = new AlertDialog.Builder(WearResultActivity.this);
                alertdialog.setTitle("終了確認");
                alertdialog.setMessage("終了しますか？");
                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WearResultActivity.this.moveTaskToBack(true);
                    }
                });
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertdialog.create().show();
                break;
        }

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {//dataAPIが更新されたら自動で呼び出される
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d("TAG", "DataItem deleted: " + event.getDataItem().getUri());
            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d("TAG", "DataItem changed: " + event.getDataItem().getUri());
                DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
                //variable = dataMap.get~("keyname"); で受け取る

                switch (event.getDataItem().getUri().toString()) {
                    case "scene":
                        scene = dataMap.getString("scene");
                        switch (scene) {
                            case "scene:battle":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //受け取り後の処理をここに
                                        //resultview.setText(resultstr);
                                        Intent intentplay = new Intent(WearResultActivity.this, WearPlayingActivity.class);//WearPlayingActivityへ遷移
                                        startActivity(intentplay);
                                    }
                                });
                                break;
                            case "scene:title":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //受け取り後の処理をここに
                                        //resultview.setText(resultstr);
                                        Intent intenttitle = new Intent(WearResultActivity.this, MainActivity.class);//MainActivityへ遷移
                                        startActivity(intenttitle);
                                    }
                                });
                                break;
                            case "scene:exit":
                                this.moveTaskToBack (true);
                                break;
                        }
                    case "result":
                        resultwl = dataMap.getString("result");
                        switch (resultwl){
                            case "win":
                                btn_win.setVisibility(View.VISIBLE);//btnwinのvisiblityをvisibleに
                                btn_lose.setVisibility(View.INVISIBLE);//btnloseのvisiblityをinvisibleに
                                break;
                            case "lose":
                                btn_lose.setVisibility(View.VISIBLE);//btnloseのvisiblityをvisibleに
                                btn_win.setVisibility(View.INVISIBLE);//btnwinのvisiblityをinvisibleに
                                break;
                        }

                }
            }
        }
    }



    @Override
    public void onConnected(Bundle bundle){
        Log.d("TAG", "onConnected");
    }
    @Override
    public void onConnectionSuspended(int i){
        Log.d("TAG", "onConnectionSuspended");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.e("TAG", "onConnectionFailed");
    }



}
