package jagsc.org.abc2016springclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,MessageApi.MessageListener,DataApi.DataListener  {

    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;

    private BluetoothClient bluetoothClient = new BluetoothClient(this);

    private ProgressDialog waitDialog;
    private String errorMessage = "";

    private GoogleApiClient mGoogleApiClient;
    private TextView resultview;
    private String resultstr;

    private boolean connected_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build();
        resultstr="";

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(bluetoothClient != null && bluetoothClient.is_connected()){
            bluetoothClient.doClose();
        }
        connected_ = false;

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
        if(bluetoothClient.is_inited()){
            bluetoothClient.doConnect(null);
        }else{
            // Bluetooth初期化
            bluetoothClient.init();
            // ペアリング済みデバイスの一覧を表示してユーザに選ばせる。
            showDialog(DEVICES_DIALOG);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothClient != null && bluetoothClient.is_connected()){
            bluetoothClient.doClose();
        }
    }

    //----------------------------------------------------------------
    // 以下、ダイアログ関連
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DEVICES_DIALOG) return createDevicesDialog();
        if (id == ERROR_DIALOG) return createErrorDialog();
        return null;
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == ERROR_DIALOG) {
            ((AlertDialog) dialog).setMessage(errorMessage);
        }
        super.onPrepareDialog(id, dialog);
    }

    public Dialog createDevicesDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select device");

        // ペアリング済みデバイスをダイアログのリストに設定する。
        Set<BluetoothDevice> pairedDevices = bluetoothClient.getPairedDevices();
        final BluetoothDevice[] devices = pairedDevices.toArray(new BluetoothDevice[0]);
        String[] items = new String[devices.length];
        for (int i=0;i<devices.length;i++) {
            items[i] = devices[i].getName();
        }

        alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 選択されたデバイスを通知する。そのまま接続開始。
                bluetoothClient.doConnect(devices[which]);
            }
        });
        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();
    }

    @SuppressWarnings("deprecation")
    public void errorDialog(String msg) {
        if (this.isFinishing()) return;
        this.errorMessage = msg;
        this.showDialog(ERROR_DIALOG);
    }
    public Dialog createErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        return alertDialogBuilder.create();
    }

    public void showWaitDialog(String msg) {
        if (waitDialog == null) {
            waitDialog = new ProgressDialog(this);
        }
        waitDialog.setMessage(msg);
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.show();
    }
    public void hideWaitDialog() {
        waitDialog.dismiss();
        connected_ = true;
        resultview.setText("connected");
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //mGoogleApiClient.disconnect();
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        if(bluetoothClient != null && bluetoothClient.is_connected()){
            bluetoothClient.doClose();
            connected_=false;
        }
    }

    @Override
    public void onConnected(Bundle bundle){
        Log.d("TAG", "onConnected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);

    }
    @Override
    public void onConnectionSuspended(int i){
        Log.d("TAG", "onConnectionSuspended");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.e("TAG", "onConnectionFailed");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/sensordata")) {
            resultstr = new String(messageEvent.getData());
            if(connected_){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultview.setText(resultstr);
                        SendSensorNum(resultstr);
                    }
                });
            }
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d("TAG", "DataItem deleted: " + event.getDataItem().getUri());
            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d("TAG", "DataItem changed: " + event.getDataItem().getUri());
                DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
                //variable = dataMap.get~("keyname"); で受け取る

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //受け取り後の処理をここに
                        //resultview.setText(resultstr);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SendSensorNum(String sendstr){
        bluetoothClient.doSend(sendstr);
        bluetoothClient.doReceive();

    }

}
