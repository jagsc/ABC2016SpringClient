package jagsc.org.abc2016springclient;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);

    //private GoogleApiClient mGoogleApiClient;
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    //private Button btn_start;//result画面への遷移のボタン
    private String scene;//dataAPIのシーン情報のkey
    private boolean ready;//準備完了状態
    private Button btn_exit;
    private AlertDialog.Builder alertdialog;

    private GlobalVariables globalv;

    private int state;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    boolean connection_state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_wear);//起動時にactivity_title_wearを表示する
        setAmbientEnabled();

        globalv = (GlobalVariables) this.getApplication();

        globalv.mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();


        btn_exit = (Button) findViewById(R.id.btn_exit_tit);
        btn_exit.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView_ready);


        if (connection_state) {
            ready = true;
            mTextView.setText("～準備完了～");
        } else {
            ready = false;
            mTextView.setText("Bluetoothの設定を行ってから再度実行してください");
        }


        /*mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        //btn_start=(Button)findViewById(R.id.btn_to_start);
        //btn_start.setOnClickListener(this);

        button_result=(Button)findViewById(R.id.btn_to_result);//デバッグ用
        button_result.setOnClickListener(this);//デバッグ用
    */
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        globalv.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://jagsc.org.abc2016springclient/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        if (globalv.mGoogleApiClient != null && globalv.mGoogleApiClient.isConnected()) {
            globalv.mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://jagsc.org.abc2016springclient/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
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
    */
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_tit:

                alertdialog = new AlertDialog.Builder(MainActivity.this);
                alertdialog.setTitle("終了確認");
                alertdialog.setMessage("終了しますか？");
                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.moveTaskToBack(true);
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
        /*switch (view.getId()){
            case R.id.btn_to_result://btn_to_resultボタンが押された
                Intent intent = new Intent(this, WearResultActivity.class);//WearResultActivityに遷移
                intent.putExtra("Connection", (Serializable) mGoogleApiClient);//データ確立情報が次のactivityに送られる？
                startActivity(intent);
                break;
          }*/


    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG", "onConnectionSuspended");


    }


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
                                        Intent intentplay = new Intent(MainActivity.this, WearPlayingActivity.class);//WearPlayingActivityへ遷移
                                        startActivity(intentplay);
                                    }
                                });
                                break;
                            case "scene:result":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //受け取り後の処理をここに
                                        //resultview.setText(resultstr);
                                        Intent intentresult = new Intent(MainActivity.this, WearResultActivity.class);//WearRsultActivityへ遷移
                                        startActivity(intentresult);
                                    }
                                });
                                break;
                        }
                }

            }
        }
    }

    public void onConnected(Bundle savedInstanceState) {
        Log.d("TAG", "onConnected");
        globalv.mGoogleApiClient.isConnected();

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionFailed");
        globalv = (GlobalVariables) this.getApplication();

    }

    public void onMessageReceived(MessageEvent messageEvent) {

    }
}
