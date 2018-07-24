package com.example.youjie.barcode;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SimpleTimeZone;
import com.android.scanner.impl.ReaderManager;

import static com.example.youjie.barcode.SystemBroadCast.SCN_CUST_ACTION_SCODE;
import static com.example.youjie.barcode.SystemBroadCast.SCN_CUST_EX_SCODE;

/**
 * TODO: 产成品单
 * @Author:clb
 * @date:2018-7-23日下午16点54分
 */
public class EnterFormActivity extends BaseActivity {
    private WebView webView;
    private ReaderManager readerManager;// 扫码

    public AlertDialog alertDialogone;
    public AlertDialog alertDialogtwo;
    public AlertDialog alertDialogefour;// 失败弹窗
    public AlertDialog alertDialogEx;// 单据失败提醒弹窗
    public AlertDialog dialog;// 单据失败提醒弹窗
    public AlertDialog alertDialogreset;//取消的弹框

    public String message;//扫描得到的条码

    public AlertDialog.Builder loading;// 加载动作
    // 重写返回键的监听事件
    @Override
    public void onBackPressed() {
        String isback = "";
        if(isback.equals("")){
            //此处写退向后台的处理
            Intent intent = new Intent(EnterFormActivity.this,EnterListActivity.class);
            startActivity(intent);
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("您确定返回吗?")
                    .setIcon(R.mipmap.tishi)
                    .setCancelable(false)
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //取消操作
                        }
                    })
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Toast.makeText(EnterFormActivity.this, "数据已暂存到您本地！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EnterFormActivity.this,EnterListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .create().show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //物理扫描按键是否有效设置
        readerManager = ReaderManager.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_form);
        webView = (WebView) findViewById(R.id.wv_enterForm);
        //隐藏logo名
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        webView.loadUrl("file:///android_asset/enter_form.html");
        //开启js
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSBridge(),"jsbridge");
        //扫码
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver,intentFilter);

        // 页面加载完成后
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //获取表头数据json回显在页面上
               /* final OverDataAll jsonH = (OverDataAll)getApplication();
                String jsonHeadertwo = jsonH.getJsonHeader();
                webView.loadUrl("javascript:databacktwo("+jsonHeadertwo+")");*/

                // TODO: 2018/7/6 获取参照数据
                Map proc = new HashMap();
                proc.put("getc", "123456");
                WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_S_CK", (HashMap) proc,new WebService.WebServiceCallBack() {
                    //WebService接口返回的数据回调到这个方法中
                    @Override
                    public void callBack(SoapObject result) {
                        if(result != null){
                            String jsondatack = result.getProperty(0).toString();
                            webView.loadUrl("javascript:ckdataback("+jsondatack+")");
                            //将数存入全局变量
                        }else{
                            // TODO: 操作
                        }
                    }
                });
            }
        });
        //弹框two  是否返回
        /*alertDialogtwo = new AlertDialog.Builder(this)
                .setTitle("是否保存数据?")
                .setIcon(R.mipmap.tishi)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 开启扫码
                        readerManager.setEnableScankey(true);
                        Toast.makeText(OutputFormActivity.this, "已保存您的数据！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OutputFormActivity.this,OutputListActivity.class);
                        startActivity(intent);
                        alertDialogtwo.hide();
                    }
                })
                .setNegativeButton("算了，谢谢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 开启扫码
                        readerManager.setEnableScankey(true);
                        *//*第一步、调用接口删除单据中的明细，如果接口返回成功，删除本地sqllite的数据*//*
                        *//*第二步，更新列表*//*
                        //调用wwebservice
                        final OverDataAll apps_r = (OverDataAll)getApplication();
                        String gid_r = apps_r.getGformid();
                        Map procg = new HashMap();
                        procg.put("strid",gid_r);
                        WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_R_VOUCHERDEL", (HashMap) procg,new WebService.WebServiceCallBack() {
                            //WebService接口返回的数据回调到这个方法中
                            @Override
                            public void callBack(SoapObject result) {
                                // 返回状态 1 成功，0失败
                                //但是不管数据库中数据删除成功还是不成功，本地sqlite都要删除
                                // 由于弹框事件中操作sqlite的局限性，
                            }
                        });
                        // << 如果确定返回，删除刚插入暂存进sqllite的单据数据 条件是gfirmids
                        // 获取gformid
                        final OverDataAll apps = (OverDataAll)getApplication();
                        String gid_ = apps.getGformid();
                        //通过gformid删除数据
                        // 创建DatabaseHelper对象
                        DBHelperH dbHelperdel = new DBHelperH(OutputFormActivity.this);
                        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                        SQLiteDatabase sqliteDatabasedel = dbHelperdel.getWritableDatabase();
                        //删除数据
                        sqliteDatabasedel.delete("tableh1", "gfid=?", new String[]{gid_});
                        //关闭数据库
                        sqliteDatabasedel.close();
                        Intent intent = new Intent(OutputFormActivity.this,OutputListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create();
*/

        // 友好的错误处理
      /*  alertDialogefour = new AlertDialog.Builder(this)
                .setTitle("错误:")
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialogefour.dismiss();
                    }
                }).create();*/

        // 保存错误弹框
       /* alertDialogEx = new AlertDialog.Builder(OutputFormActivity.this)
                .setTitle("异常信息:")
                .setIcon(R.mipmap.err)
                .setCancelable(false)
                // 必须点击确定才能关闭
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialogEx.dismiss();
                        // 开启扫码
                        readerManager.setEnableScankey(true);
                    }
                }).create();*/

        //取消提示框
       /* alertDialogreset = new AlertDialog.Builder(this)
                .setTitle("是否放弃当前单据?")
                .setIcon(R.mipmap.tishi)
                .setCancelable(false)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(OutputFormActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        alertDialogreset.hide();
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(OutputFormActivity.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();*/
    }

    /* TODO: 安卓<==>JS交互*/
    public class JSBridge{

        // TODO: 2018/7/13 修改仓库
        /*
         * 修改的时候要先判断表头是否有值，如果没有则直接不修改数据，如果有，通过gformid唯一值修改仓库。
         * 替换掉数据库中gformid等于当前的这条值
         * */
        /*@JavascriptInterface
        public void editHead(String jsonHval){
            final OverDataAll JH = (OverDataAll)getApplication();
            String jhdata = JH.getJsonHeader();
            try {
                JSONObject jh = new JSONObject(jhdata.toString());
                // 获取发货单号
                Iterator<String> sIterator = jh.keys();
                while(sIterator.hasNext()){
                    String key = sIterator.next();
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    gformidedit = jh.getString("gformid");
                }
            }catch (JSONException e){
                // TODO: 2018/7/13
            }
            // 存在表头数据，开始更新sqlite中的数据
            if(!jhdata.equals("")){
                // <<加入暂存列表 使用SqlLite>>//
                // 创建数据库
                DBHelperH dbHelperedit = new DBHelperH(OutputFormActivity.this);
                // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase sqliteDatabaseedit = dbHelperedit.getWritableDatabase();
                SQLiteDatabase sqliteDatabaseedits = dbHelperedit.getReadableDatabase();
                // 判断是否存在数据
                String sql = "select count(*) as c from tableh1 where gfid = '"+gformidedit+"'";
                Cursor newCursors = sqliteDatabaseedits.rawQuery(sql, null);
                if(newCursors.moveToNext()){
                    int count = newCursors.getInt(0);
                    if(count>0){
                        try {
                            //JSONArray jsarr = new JSONArray(jsonHval);
                            JSONObject objects_ = new JSONObject(jsonHval);
                            ArrayList arr = new ArrayList();
                            arr.add(objects_);
                            // 创建数据集
                            ContentValues evalue = new ContentValues();
                            evalue.put("hcontent", arr.toString());
                            //更新数据
                            sqliteDatabaseedit.update("tableh1",evalue,"gfid=?",new String[]{gformidedit});
                            Toast.makeText(OutputFormActivity.this, "仓库更改成功！", Toast.LENGTH_SHORT).show();
                        }catch (JSONException e){
                            e.getStackTrace();
                            // TODO: 2018/7/13
                        }
                    }
                }
                //关闭数据库
                sqliteDatabaseedits.close();
            }
        }*/
        //消息弹出层

        /**
         * TODO: 返回上个页面
         * @param str
         * 页面是否存在数据，一个状态
         */
        @JavascriptInterface
        public void back(String str){
            str = "0";
            readerManager.setEnableScankey(false);
            // 清除已扫明细
            ArrayList<String> arrayList = new ArrayList<String>();
            if(str.equals("1")){
                //alertDialogtwo.show();
            }else{
                readerManager.setEnableScankey(true);
                Intent intent = new Intent(EnterFormActivity.this,EnterListActivity.class);
                startActivity(intent);
                finish();
            }
        }
        /**
         * TODO: 获取表单值
         * @param jsonHval
         * 表头值集合
         */
        @JavascriptInterface
        public void dataHform(String jsonHval){

        }

        /**
         * TODO: 获取表单值
         * @param jsonHval
         * 表头值集合
         */
        @JavascriptInterface
        public void jumppage(String jsonHval){
            //开启扫码
            readerManager.setEnableScankey(true);
            Intent intent = new Intent(EnterFormActivity.this,EnterScanActivity.class);
            startActivity(intent);
            finish();
        }

        // TODO: 取消
        @JavascriptInterface
        public void resetsave(String str){
            if(str.equals("1")){
                new AlertDialog.Builder(EnterFormActivity.this)
                        .setTitle("是否放弃当前单据?")
                        .setIcon(R.mipmap.tishi)
                        .setCancelable(false)
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        })
                        .create().show();
            }else{
                Intent intent = new Intent(EnterFormActivity.this,EnterFormActivity.class);
                startActivity(intent);
                finish();
            }

        }

        // TODO: 2018/7/6 保存生成单据
        @JavascriptInterface
        public void savedata(final String gformid){
            loading = new AlertDialog.Builder(EnterFormActivity.this);
            loading.setIcon(R.mipmap.load1);
            loading.setCancelable(false);
            loading.setTitle("温馨提示");
            loading.setMessage("正在处理数据,勿重复提交，请稍后......");
            dialog = loading.show();

            // 判断特殊情况，断电，电网，特殊处理
            // <<1.检查网络状态
            // <<2.如果断网断电，要做出的处理
            /*final OverDataAll DataTable = (OverDataAll)getApplication();
            //  获取表头，表体数据
            jsonHeader = DataTable.getJsonHeader();
            arrayList = DataTable.getArrayList();
            // 循环取表头
            try {
                JSONObject jsonHBObj = new JSONObject(jsonHeader);
                // 获取发货单号
                Iterator<String> sIterator = jsonHBObj.keys();
                while(sIterator.hasNext()){
                    // 获得key
                    String key = sIterator.next();
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    statues = jsonHBObj.getString("statues");//
                    bredvouch = jsonHBObj.getString("bredvouch");//bredvouch 蓝字红字
                    dlid = jsonHBObj.getString("dlid");//条形码
                    ccusabbname = jsonHBObj.getString("ccusabbname");//客户
                    ccuscode = jsonHBObj.getString("ccuscode");//
                    crdcode = jsonHBObj.getString("crdcode");//
                    crdname = jsonHBObj.getString("crdname");//出库类别
                    cdlcode = jsonHBObj.getString("cdlcode");//发货单号
                    cpersonname = jsonHBObj.getString("cpersonname");//业务员
                    cpersoncode = jsonHBObj.getString("cpersoncode");//
                    cdepname = jsonHBObj.getString("cdepname");//部门
                    cdepcode = jsonHBObj.getString("cdepcode");//
                    cwhname = jsonHBObj.getString("cwhname");//仓库
                    cwhcode = jsonHBObj.getString("cwhcode");//
                    gformids = jsonHBObj.getString("gformid");//
                    cmemo = jsonHBObj.getString("cmemo");//备注
                }
            }catch (JSONException e){
                Toast.makeText(OutputFormActivity.this, "出错了", Toast.LENGTH_SHORT).show();
            }*/
            // 判断特殊情况，断电，电网，特殊处理

            // 发货单号参数
            //调用生成单据接口
            Map pro = new HashMap();
           /* pro.put("strcode", cdlcode+gformids);
            if(bredvouch.equals("0")){
                pro.put("strType","0303");
            }else{
                pro.put("strType","10303");
            }*/
            // TODO: 2018/7/6 生成单据
            WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_R_Voucher", (HashMap) pro,new WebService.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBack(SoapObject result) {
                    // 创建弹框
                    AlertDialog.Builder builder= new AlertDialog.Builder(EnterFormActivity.this);
                    builder.setCancelable(true);
                    if(result != null){
                        String jsondatas = result.getProperty(0).toString();
                        String msg = result.getProperty(1).toString();
                        if(jsondatas.equals("1")){
                            // << 如果成功，删除刚插入暂存进sqllite的单据数据 条件是gfirmids
                            // 获取gformid
                           /* final OverDataAll apps = (OverDataAll)getApplication();
                            String gid_ = apps.getGformid();
                            *//*通过gformid删除数据*//*
                            // 创建DatabaseHelper对象  保存成功后删除暂存数据
                            DBHelperH dbHelperdel = new DBHelperH(OutputFormActivity.this);
                            // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                            SQLiteDatabase sqliteDatabasedel = dbHelperdel.getWritableDatabase();
                            //删除数据
                            sqliteDatabasedel.delete("tableh1", "gfid=?", new String[]{gid_});
                            //关闭数据库
                            sqliteDatabasedel.close();*/

                            new AlertDialog.Builder(EnterFormActivity.this)
                                    .setTitle("单据生成成功！")
                                    .setIcon(R.mipmap.oks)
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            // 开启扫码
                                            readerManager.setEnableScankey(true);
                                            // 清除表头数据
                                           /* final OverDataAll OverdataH = (OverDataAll)getApplication();
                                            OverdataH.setJsonHeader("");*/
                                            Intent intent = new Intent(EnterFormActivity.this,EnterListActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).create().show();

                        }else{
                            // 保存失败提示
                            builder.setIcon(R.mipmap.err);
                            builder.setTitle("错误：单据生成失败！");//设置标题
                            builder.setMessage(msg);

                        }
                    }else{
                        builder.setIcon(R.mipmap.err2);
                        builder.setTitle("异常信息");//设置标题
                        builder.setMessage("数据异常,请稍后再试！");

                    }
                    // 弹出框显示
                    builder.show();

                    // 加载条隐藏loading
                    dialog.dismiss();
                }
            });
        }
    }

    // TODO: 2018/7/6 扫描处理
    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 单独进页面只允许扫描一次，若要扫描第二次，重新进入页面即可关闭扫码
            readerManager.setEnableScankey(false);
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                try {
                    //条码回填
                    message = intent.getStringExtra(SCN_CUST_EX_SCODE).toString();
                    webView.loadUrl("javascript:show('"+message+"')");
                    //条码请求接口回填
                    Map proH = new HashMap();
                    //proH.put("strCode", "0000106708");
                    proH.put("strCode", message);
                    proH.put("strType", "H");
                    proH.put("strPackCode", "");
                    // TODO: 2018/7/6 请求表头接口
                    WebService.callWebService(WebService.WEB_SERVER_URL,null, "U8_S_XSCK", (HashMap) proH,new WebService.WebServiceCallBack() {
                        //WebService接口返回的数据回调到这个方法中
                        @Override
                        public void callBack(SoapObject result) {
                            // 随机取三位字母
                            BaseActivity a = new BaseActivity();
                            String randstr = a.getRandStr(3);
                            //获取机型
                            SystemUtil systemUtil = new SystemUtil();
                            String model = systemUtil.getPesudoUniqueID();
                            if(result != null){
                                String status = result.getProperty(0).toString();
                                String errmessage = result.getProperty(2).toString();
                                //jsondata = result.getProperty(1).toString();
                                if(status.equals("1")){
                                   /* try {
                                        //获取系统时间的10位的时间戳
                                        long time = System.currentTimeMillis() / 1000;
                                        timedate = String.valueOf(time);
                                        JSONArray jsonArray = new JSONArray(jsondata);
                                        JSONObject objects = jsonArray.getJSONObject(0);
                                        objects.put("gformid",randstr+timedate);
                                        gid = objects.getString("gformid");
                                        // 将唯一号存入全局变量
                                        final OverDataAll app = (OverDataAll)getApplication();
                                        app.setGformid(gid);
                                        cdlcodeback = objects.getString("cdlcode");

                                        webView.loadUrl("javascript:databack("+jsonArray+")");

                                        // <<加入暂存列表 使用SqlLite>>//
                                        // 创建数据库
                                        DBHelperH dbHelper = new DBHelperH(OutputFormActivity.this);
                                        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                                        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
                                        SQLiteDatabase sqliteDatabasered = dbHelper.getReadableDatabase();
                                        // 判断是否存在数据
                                        String hcontent = jsonArray.toString();
                                        String sql = "select count(*) as c from tableh1 where hbid = '"+cdlcodeback+"'";
                                        Cursor newCursor = sqliteDatabasered.rawQuery(sql, null);
                                        if(newCursor.moveToNext()){
                                            int count = newCursor.getInt(0);
                                            if(count>0){
                                            }else {
                                                // 创建ContentValues对象
                                                ContentValues valuesH = new ContentValues();
                                                // 失败单据的时间设置
                                                // 向该对象中插入键值对 表头数据
                                                valuesH.put("hbid", cdlcodeback);
                                                valuesH.put("errdate", timedate);
                                                valuesH.put("gfid", gid);
                                                valuesH.put("hcontent", hcontent);
                                                // 调用insert()方法将数据插入到数据库当中
                                                sqliteDatabase.insert("tableh1", null, valuesH);
                                            }
                                        }
                                        //关闭数据库
                                        sqliteDatabase.close();

                                    }catch (Exception e){

                                    }
                                    webView.loadUrl("javascript:formHdata()");*/
                                }else {
                                    //Toast.makeText(OutputFormActivity.this, "播放声音！！!", Toast.LENGTH_SHORT).show();
                                    alertDialogEx.setMessage(errmessage);
                                    alertDialogEx.show();
                                    webView.loadUrl("javascript:databackclear()");
                                    webView.loadUrl("javascript:show('请重新扫码')");
                                    // 关闭扫码
                                    readerManager.setEnableScankey(false);
                                }
                            }else{
                                // 开启扫码
                                readerManager.setEnableScankey(true);
                                AlertDialog.Builder neterr = new AlertDialog.Builder(EnterFormActivity.this);
                                neterr.setIcon(R.mipmap.neterr);
                                neterr.setCancelable(true);
                                neterr.setTitle("网络错误");
                                neterr.setMessage("数据异常,请检查网络或稍后再试！");
                                neterr.show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
