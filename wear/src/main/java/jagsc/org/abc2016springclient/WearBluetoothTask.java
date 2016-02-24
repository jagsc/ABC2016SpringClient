package jagsc.org.abc2016springclient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by yuuki on 2016/02/24.
 */
public class WearBluetoothTask extends Activity {
     String strstate;

    private BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 見つけたデバイス情報の取得
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // インテントフィルタの作成
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // ブロードキャストレシーバの登録
        registerReceiver(mReceiver, filter);
        // BluetoothAdapterのインスタンス取得
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Bluetooth有効
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }
    }
    public String getBondState(int state) {
        switch (state) {
            case BluetoothDevice.BOND_BONDING://BOND_BONDINGならstrstateに接続中の文字を入れる
                strstate = "接続中";
                break;
            default :
                strstate = "エラー";//接続中でなければstrstateにエラーの文字を入れる
        }
        return strstate;
    }
}
