package com.example.youjie.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;
import com.android.scanner.impl.ReaderManager;
/**
 * TODO: 2018/7/23
 * @Auther:clb
 * @date:16点20分
 * */
public class EnterListActivity extends BaseActivity{
    private ReaderManager readerManager;// 扫码
    private WebView webView;

    // 记住密码存储字段
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
        Intent intent = new Intent(EnterListActivity.this,MainInterface.class);
        startActivity(intent);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        //物理扫描按键是否有效设置
        readerManager = ReaderManager.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_list);
        //隐藏logo名
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        webView = (WebView) findViewById(R.id.wv_EnterList);
        webView.loadUrl("file:///android_asset/enter_list.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        // 页面加载完成时，展示销售出库列表
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //获取用户名
                String User = pref.getString("account_name",null);
                //调用出库单列表接口
                Map proc = new HashMap();
                proc.put("cuser_name",User);
                proc.put("strType",1);
                WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_S_CKLIST", (HashMap) proc,new WebService.WebServiceCallBack() {
                    //WebService接口返回的数据回调到这个方法中
                    @Override
                    public void callBack(SoapObject result) {
                        if(result != null){
                            // 返回状态 1 成功，0失败
                            String status = result.getProperty(0).toString();
                            String jsondatack = result.getProperty(1).toString();
                            // 错误信息
                            String msg = result.getProperty(2).toString();
                            if(status.equals("1")){
                                try {
                                    JSONArray jsonArray = new JSONArray(jsondatack);
                                    String listnumsale =  String.valueOf(jsonArray.length());
                                    webView.loadUrl("javascript:listnumsale("+listnumsale+")");
                                }catch (JSONException e){
                                    e.getStackTrace();
                                }
                                /*final OverDataAll app = (OverDataAll)getApplication();
                                ArrayList arrlistsale = app.getArrayListSale();
                                webView.loadUrl("javascript:backsaledata("+jsondatack+")");*/
                            }else {

                            }

                        }else{
                            AlertDialog.Builder neterr = new AlertDialog.Builder(EnterListActivity.this);
                            neterr.setIcon(R.mipmap.neterr);
                            neterr.setCancelable(true);
                            neterr.setTitle("网络错误");
                            neterr.setMessage("数据异常,请检查网络或稍后再试！");
                            neterr.show();
                        }
                    }
                });

            }
        });
    }

    /* TODO: 安卓<==>JS交互*/
    public class JSBridge{
        /**
         * TODO:跳转详情
         * @param jsondata
         * 页面筛选数据
         * @param arrjsonshow
         * 单条数据
         * */
        @JavascriptInterface
        public void jumppage(String jsondata,String arrjsonshow){
            // 关闭扫码，离开页面开启
            readerManager.setEnableScankey(false);
            /*Intent intent = new Intent(OutputListActivity.this,TempShowActivity.class);
            startActivity(intent);
            finish();*/
        }

        /**
         * TODO:详情展示
         * @param ccodes
         * 出库单号
         * */
        @JavascriptInterface
        public void jumpdetail(String ccodes){
            // 保存ccodes   出库单号=>全局变量
           /* final OverDataAll ccodesapp = (OverDataAll)getApplication();
            ccodesapp.setCcodes(ccodes);
            Intent intent = new Intent(OutputListActivity.this,OutputDetail.class);
            startActivity(intent);
            finish();*/
        }

        // TODO: 返回上个页面
        @JavascriptInterface
        public void back(){
            Intent intent = new Intent(EnterListActivity.this,MainInterface.class);
            startActivity(intent);
        }

        // TODO: 点击新增时，需要对一些数据初始化
        @JavascriptInterface
        public void add(){
            // 首次进入页面 开启扫码功能
            //开启扫码
            readerManager.setEnableScankey(true);
           /* final OverDataAll Overdata = (OverDataAll)getApplication();
            // 清除页面内容
            Overdata.setJsonHeader("");

            // 清除已扫明细
            ArrayList<String> arrayList = new ArrayList<String>();
            Overdata.setArrayList(arrayList);

            // 清除仓库参照
            ArrayList<String> arrcz = new ArrayList<String>();
            Overdata.setArraycz(arrcz);

            // 清除明细数量
            Overdata.setListnum("0");*/

            Intent intent = new Intent(EnterListActivity.this,EnterFormActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
