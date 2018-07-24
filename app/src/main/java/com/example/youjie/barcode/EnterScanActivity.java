package com.example.youjie.barcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.android.scanner.impl.ReaderManager;

import static com.example.youjie.barcode.SystemBroadCast.SCN_CUST_ACTION_SCODE;
import static com.example.youjie.barcode.SystemBroadCast.SCN_CUST_EX_SCODE;

/**
 * TODO: 2018/7/23
 * 产成品入库单 扫码
 * @Author:CLB
 * @date:2018-7-23日下午5点30分
 */
public class EnterScanActivity extends BaseActivity {
    private ReaderManager readerManager;// 扫码
    private WebView webView;
    public String message;//条码编号
    public String jsonHeader;
    public String listnum;//明细数量
    public String dlid;//条形码
    public String cwhcode;//条形码
    public String cdepcode; //条形码
    public String LoginUser;//用户名
    public String gformid;// 唯一号

    public AlertDialog alertDialogerr;// 失败弹出框
    public AlertDialog alertDialogwin;// 成功弹框
    public AlertDialog dialog;
    public AlertDialog.Builder loading;// 加载框

    public String cvalue;

    public String idlsid;

    // 获取用户名
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
        Intent intent = new Intent(EnterScanActivity.this,EnterFormActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //物理扫描按键是否有效设置
        readerManager = ReaderManager.getInstance();
        // 取用户名
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        LoginUser = pref.getString("account_name",null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanenter);
        //隐藏logo名
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //webview
        webView = (WebView) findViewById(R.id.wv_scanenter);
        webView.loadUrl("file:///android_asset/scan_enter.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        //开启弹窗
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
        //扫码
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver,intentFilter);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // 传入页面明细数量
                //webView.loadUrl("javascript:backlistnum_("+listnum+")");
            }
        });

        // 弹框提示，成功
        alertDialogwin = new AlertDialog.Builder(this)
                .setTitle("插入成功！")
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialogerr.hide();
                    }}).create();

        // 弹框提示，失败
        alertDialogerr = new AlertDialog.Builder(this)
                .setTitle("异常信息:")
                .setIcon(R.mipmap.err)
                .setCancelable(false)//必须点击确定才能关闭
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialogerr.dismiss();
                        readerManager.setEnableScankey(true);
                    }}).create();
    }

    /**
     * js和android交互类
     */
    public class JSBridge{
        //返回
        @JavascriptInterface
        public void back(){
            Intent intent = new Intent(EnterScanActivity.this,EnterFormActivity.class);
            startActivity(intent);
            finish();
        }
        //跳转已扫明细
        @JavascriptInterface
        public void jumppagelist(){
            Intent intent = new Intent(EnterScanActivity.this,EnterSMListActivity.class);
            startActivity(intent);
            finish();
        }

        //获取表单值并和表头值组装请求接口保存数据
       /* @JavascriptInterface
        public void dataBform(String jsonBval){
            //表头表体合并为一个json对象
            try {
                JSONObject jsonObjectHB = new JSONObject(jsonHeader);
                JSONObject jsonObjectB = new JSONObject(jsonBval);
                Iterator iterator = jsonObjectB.keys();
                while(iterator.hasNext()){
                    String key = (String) iterator.next();
                    Object value = jsonObjectB.get(key);
                    jsonObjectHB.put(key,value);
                }

                jsonObjectHB.put("cmaker",LoginUser.toString());
                jsonObjectHB.put("barcode",message);
                jsonObjectHB.put("gformid",gformid);
                //添加进明细list
                final OverDataAll data = (OverDataAll)getApplication();
                ArrayList arrayList = data.getArrayList();
                arrayList.add(jsonObjectHB.toString());
                data.setArrayList(arrayList);
                data.setListnum(String.valueOf(arrayList.size()));
                //调用接口
                Map pro = new HashMap();
                // 加bredvouch蓝字还是红字   barcode 条形码参数
                pro.put("strJson", jsonObjectHB.toString());
                WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_R_XSCKSava", (HashMap) pro,new WebService.WebServiceCallBack() {
                    //WebService接口返回的数据回调到这个方法中
                    @Override
                    public void callBack(SoapObject result) {
                        if (result != null) {
                            String jsondata = result.getProperty(0).toString();
                            if (jsondata.equals("1")) {
                                Toast.makeText(EnterScanActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EnterScanActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(EnterScanActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                        }
                        readerManager.setEnableScankey(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
    //扫描处理
    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loading = new AlertDialog.Builder(EnterScanActivity.this);
            loading.setIcon(R.mipmap.load1);
            loading.setCancelable(false);
            loading.setTitle("温馨提示");
            loading.setMessage("正在处理数据,请稍后......");
            dialog = loading.show();
            // 禁止扫码
            readerManager.setEnableScankey(false);
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                try {
                    message = intent.getStringExtra(SCN_CUST_EX_SCODE).toString();
                    JSONObject jsonHBObj = new JSONObject(jsonHeader);
                    jsonHBObj.put("idlsid",message);
                    // 获取发货单号
                    Iterator<String> sIterator = jsonHBObj.keys();
                    while(sIterator.hasNext()){
                        // 获得key
                        String key = sIterator.next();
                        // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                        cvalue = jsonHBObj.getString("cdlcode");
                        dlid = jsonHBObj.getString("dlid");//条形码
                        cwhcode = jsonHBObj.getString("cwhcode");//仓库码
                        idlsid = jsonHBObj.getString("idlsid");//
                        cdepcode = jsonHBObj.getString("cdepcode");//部门码
                    }
                    // 获取发货单号结束

                    webView.loadUrl("javascript:show('"+message+"')");

                    Map pro = new HashMap();
                    //传dlid
                    pro.put("strCode",dlid);
                    pro.put("strType", "B");
                    // 正式的时候换成扫码所得码
                    //pro.put("strPackCode","88595490000122302331");
                    pro.put("strPackCode",message);
                    pro.put("cwhcode", cwhcode); // 仓库码
                    pro.put("cdepcode", cdepcode);  // 部门码
                    //Toast.makeText(ScanActivity.this,pro.toString() , Toast.LENGTH_SHORT).show();
                    WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_S_XSCK", (HashMap) pro,new WebService.WebServiceCallBack() {
                        //WebService接口返回的数据回调到这个方法中
                        @Override
                        public void callBack(SoapObject result) {
                            if(result != null){
                                String status = result.getProperty(0).toString();
                                String errmessage = result.getProperty(2).toString();
                                alertDialogerr.setMessage(errmessage);
                                if(status.equals("1")){
                                    String jsondata = result.getProperty(1).toString();
                                    // 表体扫描数据传入页面
                                    webView.loadUrl("javascript:databackB("+jsondata+")");
                                    // js表体数据获取
                                    webView.loadUrl("javascript:formBdata()");
                                    // 保存到全局变量明细 这儿的明细主要是为数据安全处理。

                                    webView.loadUrl("javascript:show('"+message+"')");
                                    // 传入页面明细数量
                                    webView.loadUrl("javascript:backlistnum("+listnum+")");
                                }else {
                                    alertDialogerr.show();
                                    webView.loadUrl("javascript:databackclear()");
                                    webView.loadUrl("javascript:show('请重新扫码')");
                                    // 开启扫码
                                }
                            }else{
                                // 开启扫码
                                readerManager.setEnableScankey(true);
                            }
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    // 开启扫码
                    readerManager.setEnableScankey(true);
                }
            }
        }
    };
}
