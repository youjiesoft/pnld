package com.example.youjie.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainInterface extends BaseActivity {
    private Button buttonOne;
    private Button buttonTwo;

    private Button loginout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        buttonOne =(Button)findViewById(R.id.button_1);
        buttonTwo =(Button)findViewById(R.id.button_2);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainInterface.this,PurchaseListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainInterface.this,EnterListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        CheckUserLoginStatus();
        //退出功能的实现
        loginout = (Button) findViewById(R.id.button_tuichu);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = pref.edit();
                editor.clear();
                editor.commit();
                // 判段是否存在登录信息
                String isLoginUser = pref.getString("account",null);
                String isLoginpass = pref.getString("password",null);
                if(isLoginUser == null && isLoginpass == null){
                    Toast.makeText(MainInterface.this, "退出成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainInterface.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //退出结束
    }
}
