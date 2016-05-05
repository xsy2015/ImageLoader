package com.water.jxwz.customview;

import android.content.Context;
import android.content.DialogInterface;

import com.water.jxwz.R;


/**
 * 调用对话框
 * 
 * @author qiaocbao
 * @version 2014-7-25 下午5:21:36
 */
public class OpenDialog {

	private OpenDialog() {
	}

	private static class DialogHolder {
		static final OpenDialog INSTANCE = new OpenDialog();
	}

	public static OpenDialog getInstance() {
		return DialogHolder.INSTANCE;
	}

	private Object readResolve() {
		return getInstance();
	}

	/**
	 * 弹出一个确定对话框，无操作
	 * 
	 * @param context
	 * @param content
	 */
	public void showDialog(Context context, String content) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
				builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
				builder.setPositiveButton(context.getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回一个对话框Builder
	 * 
	 * @param context
	 * @param content
	 * @return
	 */
	// public CustomDialog.Builder showListenerDialog(Context context,
	// String content) {
	//
	// CustomDialog.Builder builder = new CustomDialog.Builder(context);
	// builder.setMessage(content);
	// builder.setTitle("温馨提示");
	// return builder;
	// }

	/**
	 * 弹出一个确定对话框，有监听事件
	 * 
	 * @param context
	 * @param content
	 * @param ok
	 * @param okListener
	 */
	public void showOneBtnListenerDialog(Context context, String content,
			String ok, DialogInterface.OnClickListener okListener) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
				builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
				builder.setPositiveButton(ok, okListener);
				builder.create().show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 弹出两个按钮对话框，有监听事件
	 * 
	 * @param context
	 * @param content
	 * @param ok
	 * @param okListener
	 */
	public void showTwoBtnListenerDialog(Context context, String content,
			String ok, DialogInterface.OnClickListener okListener,
			String cancel, DialogInterface.OnClickListener cancelListener) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
//				builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
				builder.setTitle("");
				builder.setPositiveButton(ok, okListener);
				builder.setNegativeButton(cancel, cancelListener);
				builder.create().show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
