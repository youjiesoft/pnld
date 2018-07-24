package com.example.youjie.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PurchaseScanListActivity extends AppCompatActivity {
    public WebView webView;

    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
        Intent intent = new Intent(PurchaseScanListActivity.this,PurchaseScanActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasescanlist);

        webView = (WebView) findViewById(R.id.wv_PurchaseScaListActivity);
        //隐藏logo名
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/Purchase_Scan_list.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        //开启弹窗
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
    }

    public class JSBridge{
        @JavascriptInterface
        public void backout(){
            Intent intent = new Intent(PurchaseScanListActivity.this,PurchaseScanActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
