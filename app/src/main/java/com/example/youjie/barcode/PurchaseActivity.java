package com.example.youjie.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PurchaseActivity extends AppCompatActivity {
    public WebView webView;

    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
        Intent intent = new Intent(PurchaseActivity.this,PurchaseListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        webView = (WebView) findViewById(R.id.wv_PurchaseActivity);
        //隐藏logo名
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/Purchase.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        //开启弹窗
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
    }

    public class JSBridge{
        @JavascriptInterface
        public void backout(){
            Intent intent = new Intent(PurchaseActivity.this,PurchaseListActivity.class);
            startActivity(intent);
            finish();
        }
        @JavascriptInterface
        public void jumppage(){
            Intent intent = new Intent(PurchaseActivity.this,PurchaseScanActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
