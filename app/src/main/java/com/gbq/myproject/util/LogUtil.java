package com.gbq.myproject.util;

import android.util.Log;

import com.gbq.myproject.BuildConfig;

/**
 * 日志
 * Created by gbq on 2018/3/1.
 */

public class LogUtil {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "Accounts";

    public static void d(String message) {
        if (DEBUG) {
            Log.d(TAG, createMessage(message));
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            message = createMessage(message);
            Log.d(TAG, "[" + tag + "] " + message);
        }
    }

    public static void d(String tag, String message, Throwable paramThrowable) {
        if (DEBUG) {
            message = createMessage(message);
            Log.d(TAG, "[" + tag + "] " + message, paramThrowable);
        }
    }

    public static void e(String message) {
        Log.e(TAG, createMessage(message));
    }

    public static void e(String tag, String message) {
        message = createMessage(message);
        Log.e(TAG, "[" + tag + "] " + message);
    }

    public static void i(String message) {
        Log.i(TAG, createMessage(message));
    }

    public static void i(String tag, String message) {
        message = createMessage(message);
        Log.i(TAG, "[" + tag + "] " + message);
    }

    public static void w(String message) {
        if (DEBUG) {
            Log.w(TAG, createMessage(message));
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            message = createMessage(message);
            Log.w(TAG, "[" + tag + "] " + message);
        }
    }

    public static void w(String tag, String message, Throwable paramThrowable) {
        if (DEBUG) {
            message = createMessage(message);
            Log.w(TAG, "[" + tag + "] " + message, paramThrowable);
        }
    }

    private static String getFunctionName() {
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements == null || stackTraceElements.length < 4) {
            return null;
        }
        return sElements[3].getFileName() + ":" + sElements[3].getMethodName() + ":" +sElements[3].getLineNumber();
    }

    private static String createMessage(String message) {
        String str = getFunctionName();
        if (str == null) {
            return message;
        }
        return str + " - " + message;
    }
}
