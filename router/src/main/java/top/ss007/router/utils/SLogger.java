package top.ss007.router.utils;

import android.util.Log;


/**
 * 默认的Logger实现
 */
public class SLogger {

    private static boolean isEnableLog=false;

    public static void showLog(boolean isShowLog) {
       isEnableLog=isShowLog;
    }

    public static void debug(String tag, String msg) {
       if (isEnableLog)
           Log.d(tag,msg);
    }

    public static void info(String tag, String msg) {
        if (isEnableLog)
            Log.i(tag,msg);
    }

    public static void warning(String tag, String msg) {
        if (isEnableLog)
            Log.w(tag,msg);
    }

    public static void error(String tag, String msg) {
        if (isEnableLog)
            Log.e(tag,msg);
    }

    public static void error(String tag, String msg, Throwable throwable) {
        if (isEnableLog)
            Log.e(tag,msg,throwable);
    }
}
