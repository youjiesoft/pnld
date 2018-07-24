package com.example.youjie.barcode;

public class SystemBroadCast {
    //扫描条码服务广播
    //Scanning barcode service broadcast.
    public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    //条码扫描数据广播
    //Barcode scanning data broadcast.
    public static final String SCN_CUST_EX_SCODE = "scannerdata";

    /*6.0 以下系统采用的开始，停止扫描广播*/
    /*6.0 the following system adopts the beginning, stop scanning broadcast.*/
    public static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    public static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";

    /*这是扫描头触发控制节点，一般控制连扫模式需要用到*/
    /*This is the scan head trigger control node, which is used to control the scan mode.*/
    private static final String SCANNER_CTRL_FILE = "/sys/devices/platform/scan_se955/se955_state";

    //使能扫描开关，控制扫描头上下电
    //Enable scanning switch to control scanning overhead.
    public static final String SCANNER_POWER = "com.android.server.scannerservice.onoff";

    public static final String SCANNER_OUTPUT_MODE = "SCANNER_OUTPUT_MODE";
}
