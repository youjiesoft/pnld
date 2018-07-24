package com.example.youjie.barcode;

import android.content.IntentFilter;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.example.youjie.barcode.SystemBroadCast.SCN_CUST_ACTION_SCODE;

/**
 * TODO:用户登录类
 * @author clb
 * @date:2018-7-25下午32点21分
 */
public class Login extends BaseActivity{
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        webView = (WebView) findViewById(R.id.login);
        webView.loadUrl("file:///android_asset/login.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        //开启弹窗
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
    }

    public class  JSBridge{
        @JavascriptInterface
        public void sss(){}
    }
}
