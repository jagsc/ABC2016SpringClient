package jagsc.org.abc2016springclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yuuki on 2015/12/12.
 */
public class WearOnemoreActivity extends WearableActivity implements View.OnClickListener {
    private Button button_title;
    private Button button_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onemore_wear);//activity_onemore_wearを表示
        button_title=(Button)findViewById(R.id.btn_title);
        button_title.setOnClickListener(this);
        button_exit=(Button)findViewById(R.id.btn_to_exit);
        button_exit.setOnClickListener(this);
    }

    //@Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_title://btn_titleが押されたら
                Intent intent = new Intent(this, MainActivity.class);//WearTitleActivityへ遷移
                startActivity(intent);
                break;
            case R.id.btn_to_exit://btn_to_exitが押されたらアプリケーション終了
                this.moveTaskToBack (true);
                break;
        }//どちらの場合もhandheldとサーバに次どうするかという命令を送らないといけない

    }

}
