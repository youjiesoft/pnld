package com.example.youjie.barcode;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO: WebService接口通信层
 * @Author:clb
 * @date:2018-5-25 下午3点15分
 */
public class WebService {
    // webservice地址
    public static final String WEB_SERVER_URL = "http://192.168.3.99:8088/APIWebService.asmx";

    // 线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    // 命名空间
    private  static String NAMESPACE = "";

    // 参数
    //public static final  Map properties = new HashMap();

    /**
     * TODO: WebService主函数请求
     * @param url
     * WebService服务器地址
     * 传入为第三方接口地址，不传为本类默认声明接口地址
     * @param   nameplace
     * 命名空间
     * @param methodName
     * WebService的调用方法名
     * 调用方法必传
     * @param properties
     * WebService的参数，选填
     * @param webServiceCallBack
     * 回调接口
     * @date:2018-5-25 下午3点15分
     */
    public static void callWebService(String url,final String nameplace,final String methodName,HashMap properties, final WebServiceCallBack webServiceCallBack) {
        // 创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url,2000);
        if(nameplace != null){
            NAMESPACE = nameplace;
        }else {
            NAMESPACE = "http://tempuri.org/";
        }
        // 创建SoapObject对象
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        // 遍历传递参数
        if (properties != null){
            for(Object propertiesObj : properties.entrySet()) {
                Map.Entry entry = (Map.Entry) propertiesObj;
                System.out.println(entry.getKey() + "=" + entry.getValue());
                soapObject.addProperty(String.valueOf(entry.getKey()),entry.getValue());
            }
        }
        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        // 设置是否调用的是.Net开发的WebService
        soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;

        // 用于子线程与主线程通信的Handler
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 将返回值回调到callBack的参数中
                webServiceCallBack.callBack((SoapObject) msg.obj);
            }

        };

        // 开启线程去访问WebService
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                SoapObject resultSoapObject = null;
                try {
                    httpTransportSE.call(NAMESPACE + methodName, soapEnvelope);
                    if (soapEnvelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                    }else {
                        resultSoapObject = null;
                    }
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    String msg = e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } finally {
                    // 将获取的消息利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(0,resultSoapObject));
                }
            }
        });
    }


    /**
     * 回调
     */
    public interface WebServiceCallBack {
        public void callBack(SoapObject result);
    }
}
