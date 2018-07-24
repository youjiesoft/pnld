package com.example.youjie.barcode;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;



/**
 * TODO:用户登录类
 * @author clb
 * @date:2018-7-25下午3点50分
 */
public class LoginActivity extends BaseActivity {
    // 用户名
    private EditText accountEdit;

    // 密码
    private EditText passwordEdit;
    private Button login;

    // 记住密码存储字段
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //用户登录
        accountEdit = (EditText)findViewById(R.id.account);
        passwordEdit = (EditText)findViewById(R.id.password);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        // 判断是否登陆
        String isLoginUser = pref.getString("account",null);
        String isLoginPass = pref.getString("password",null);
        String User = pref.getString("account_name",null);
        if(isLoginUser != null && isLoginPass != null){
            Log.d("欢迎您回来：",User + "!");
            Toast.makeText(LoginActivity.this, "欢迎您回来:"+ User + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainInterface.class);
            startActivity(intent);
            finish();
        }
        // 记住密码
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account",null);
            String password = pref.getString("password",null);
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                final String account = accountEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                //通过工具类调用WebService接口
                Map properties = new HashMap();
                properties.put("strUserCode",account);
                properties.put("strPassWord", password);
                // TODO: 调用接口登录
                WebService.callWebService(WebService.WEB_SERVER_URL,null,"U8_S_USER", (HashMap) properties,new WebService.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if(result != null){
                            try {
                                String jsondata = result.getProperty(0).toString();
                                JSONArray jsonArray = new JSONArray(jsondata);
                                /*判断用户名和密码是否为空以及是否存在*/
                                if (jsonArray.length() == 0){
                                    AlertDialog.Builder neterrs = new AlertDialog.Builder(LoginActivity.this);
                                    neterrs.setIcon(R.mipmap.sad2);
                                    neterrs.setCancelable(true);
                                    neterrs.setTitle("系统消息");
                                    neterrs.setMessage("登录失败！用户名或密码错误！！");
                                    neterrs.show();
                                }
                                Log.d("登录中：.......","正在登录");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String user = jsonObject.getString("cuser_name");
                                    String pass = jsonObject.getString("pw");
                                    if(user != null && pass != null){
                                        editor = pref.edit();
                                        editor.putString("account",account);
                                        editor.putString("account_name",user);
                                        editor.putString("password",password);
                                        if(rememberPass.isChecked()){
                                            editor.putBoolean("remember_password",true);
                                            editor.putString("isChecked","1");
                                        }else {
                                            editor.putString("isChecked","0");
                                        }
                                        editor.commit();
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, MainInterface.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            /*提示框弹出*/
                            AlertDialog.Builder neterr = new AlertDialog.Builder(LoginActivity.this);
                            neterr.setIcon(R.mipmap.sad2);
                            neterr.setCancelable(true);
                            neterr.setTitle("系统消息");
                            neterr.setMessage("登录失败！超时或用户名密码错误！！");
                            neterr.show();
                        }
                    }
                });
            }
        });
    }
}

