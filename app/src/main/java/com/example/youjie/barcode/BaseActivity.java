package com.example.youjie.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO: 基础继承类BaseActivity
 * @Author:CLB
 * @date:2018-7-23日下午3点26分
 */
public class BaseActivity extends AppCompatActivity
{
    // TODO: 用户验证存储
    private SharedPreferences pref;

    // TODO: 用户信息更改
    private SharedPreferences.Editor editor;

    // TODO: 用户名
    private String isLoginUser;

    // TODO: 用户密码
    private String isLoginpass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    // TODO: 用户登录验证 2018/5/31
    protected void CheckUserLoginStatus(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isLoginUser = pref.getString("account",null);
        isLoginpass = pref.getString("password",null);
        if(isLoginUser == null && isLoginpass == null){
            Toast.makeText(BaseActivity.this, "请您先进行登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // TODO: 2018/7/5 0:46: 23  获取时间戳
    protected String TimeStampTen(){
        long times_tamp;
        times_tamp = System.currentTimeMillis() / 1000;
        String timestamp = String.valueOf(times_tamp);
        return timestamp;
    }

    // TODO: 2018/7/5 0:51:03 时间戳转为时间
    protected String TimeStampToDate(String timestr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = simpleDateFormat.format(new Date(Long.valueOf(timestr) * 1000L));
        return datetime;
    }

    // TODO: 2018/7/12  随机取数
    public static String getRandStr(int num){

        String strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer buff = new StringBuffer();
        for(int i=1;i<=num;i++){
            char str = strs.charAt((int)(Math.random() * 26));
            buff.append(str);
        }
        return buff.toString();
    }
}
