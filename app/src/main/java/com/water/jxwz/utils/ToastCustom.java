package com.water.jxwz.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.jxwz.R;


/**
 * 自定义弹出toast(解决设置通知栏关掉后弹不出toast问题)
 * @author qiaocbao
 * @version 2015-5-26  上午10:16:09
 */
public class ToastCustom {

    private WindowManager wdm;
    private double time;
    private View mView;
    private WindowManager.LayoutParams params;
    private Timer timer;

    private ToastCustom(Context context, String text, double time) {
	timer = new Timer();

	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	mView = inflater.inflate(R.layout.custom_toast, null);
	TextView textView = (TextView) mView.findViewById(R.id.toast_content);
	textView.setText(text);
	// toast.setView(layout);
	// mView = toast.getView();

	params = new WindowManager.LayoutParams();
	params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	params.format = PixelFormat.TRANSLUCENT;
	params.windowAnimations = mView.getAnimation().INFINITE;
	params.type = WindowManager.LayoutParams.TYPE_TOAST;
	params.setTitle("Toast");
	params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
	params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
	params.y = -30;

	this.time = time;
    }

    private ToastCustom(Context context, int resId, String text, double time) {
	timer = new Timer();

	// 获取LayoutInflater对象
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// 由layout文件创建一个View对象
	mView = inflater.inflate(R.layout.custom_toast, null);

	// 实例化ImageView和TextView对象
	ImageView imageView = (ImageView) mView.findViewById(R.id.img_toast);
	TextView textView = (TextView) mView.findViewById(R.id.toast_content);
	imageView.setVisibility(View.VISIBLE);
	imageView.setImageResource(resId);
	textView.setText(text);
	// mView = toast.getView();

	params = new WindowManager.LayoutParams();
	params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	params.format = PixelFormat.TRANSLUCENT;
	params.windowAnimations = mView.getAnimation().INFINITE;
	params.type = WindowManager.LayoutParams.TYPE_TOAST;
	params.setTitle("Toast");
	params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
	params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
	params.y = -30;

	this.time = time;
    }

    public static ToastCustom makeText(Context context, String text, double time) {
	ToastCustom toastCustom = new ToastCustom(context, text, time);
	return toastCustom;
    }

    public static ToastCustom makeText(Context context, int resId, String text,
	    double time) {
	ToastCustom toastCustom = new ToastCustom(context, resId, text, time);
	return toastCustom;
    }

    public void show() {
	getWindowManager().addView(mView, params);
	timer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		getWindowManager().removeView(mView);
	    }
	}, (long) (time * 1000));
    }

    private WindowManager getWindowManager() {
	if (wdm == null) {
	    Context context = mView.getContext().getApplicationContext();
	    if (context == null) {
		context = mView.getContext();
	    }
	    wdm = (WindowManager) context
		    .getSystemService(Context.WINDOW_SERVICE);
	}
	return wdm;
    }

    public void cancel() {
	wdm.removeView(mView);
	timer.cancel();
    }

}
