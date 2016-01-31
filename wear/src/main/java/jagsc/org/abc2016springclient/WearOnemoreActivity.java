package jagsc.org.abc2016springclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

/**
 * Created by yuuki on 2015/12/12.
 */
public class WearOnemoreActivity extends WearableActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onemore_wear);//activity_onemore_wearを表示
    }

    //@Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_title://btn_to_onemoreが押されたら
                Intent intent = new Intent(this, MainActivity.class);//WearTitleActivityへ遷移
                startActivity(intent);
                break;
            case R.id.btn_to_exit://btn_to_exitが押されたらアプリケーション終了
                break;//どうやって終了させる？
        }//どちらの場合もhandheldとサーバに次どうするかという命令を送らないといけない

    }

}
