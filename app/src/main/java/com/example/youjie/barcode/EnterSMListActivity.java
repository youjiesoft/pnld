package com.example.youjie.barcode;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

/**
 * TODO: 产成品入库单-扫描明细类
 * @Author:CLB
 * @date:2018-7-24日上午10点49分
 */
public class EnterSMListActivity extends BaseActivity {
    private WebView webView;
    public String totlenum;//页面明细条数

    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
        Intent intent = new Intent(EnterSMListActivity.this,EnterScanActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entersm_list);
        webView = (WebView) findViewById(R.id.wv_entersmList);
        //隐藏logo名
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        webView.loadUrl("file:///android_asset/entersm_list.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        //开启弹窗
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // 传入页面当前页条数
                /*webView.loadUrl("javascript:databacklist("+arrayList+")");*/
            }
        });
    }

    /**
     * js和android交互类
     */
    public class JSBridge{
        //返回
        @JavascriptInterface
        public void back(){
            Intent intent = new Intent(EnterSMListActivity.this,EnterScanActivity.class);
            startActivity(intent);
            finish();
        }

        // 点进入修改页面时，传入单条数据，还有这个表格行的index,目的是把index存到全局变量，方便删除的时候获取
        @JavascriptInterface
        public void edit(String jsoneditdata,String Tindex){
            // 将表格的索引存入全局变量
         /*   final OverDataAll tindex = (OverDataAll)getApplication();
            tindex.setTindex(Tindex);

            // 将单条要修改的明细数据保存到全局变量
            final OverDataAll jsoneditData = (OverDataAll)getApplication();
            jsoneditData.setListedit(jsoneditdata);
            Intent intent = new Intent(EnterSMListActivity.this,ScanEditActivity.class);
            startActivity(intent);
            finish();*/
        }

    }
}
