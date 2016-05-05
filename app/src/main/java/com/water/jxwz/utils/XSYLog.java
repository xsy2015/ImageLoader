package com.water.jxwz.utils;

import android.util.Log;

import com.water.jxwz.base.Constant;


/**
 * Log日志输出
 * 
 * @author qiaocbao
 * @version 2014-3-31 下午2:29:57
 */
public class XSYLog {
	/** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */
	private final static Boolean DEBUG = Constant.DEBUG;
	private final static String TAG = Constant.TAG;

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}
}
