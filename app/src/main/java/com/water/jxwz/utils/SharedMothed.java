package com.water.jxwz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.water.jxwz.base.MyApplication;

//import cn.smssdk.SMSSDK;

/**
 * @Description 公用方法
 */
public class SharedMothed {
	private static Random random = new Random();
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * @Description 获取当前系统时间
	 * @return String
	 */
	public static String getCurrDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH点mm分",
				Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * @Description 获取当前系统日期时间
	 * @return String
	 */
	public static String getCurrDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}
	public static String getYearMonthDay(String data) {
		try {
			Date da = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
					Locale.getDefault()).parse(data);
			Calendar cal = Calendar.getInstance();
			cal.setTime(da);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DATE);
			return year + "-" + month + "-" + day;

		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * @Description 比较两个日期大小
	 * @param startTime
	 * @param endTime
	 * @param format
	 *            时间要为24小时制，否则大于12点时会判断错误
	 * @return boolean true:开始时间小于结束时间，false：其他
	 */
	public static boolean compareDate(String startTime, String endTime,
			SimpleDateFormat format) {
		if (format == null) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		try {
			Date startDate = format.parse(startTime);
			Date endDate = format.parse(endTime);
			if (startDate.getTime() < endDate.getTime()) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取本机手机号码，有的手机由于运营商SIM的问题获取不到手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * @Description 字符串进行unicode编码
	 * @param str
	 * @return String
	 */
	public static String convertUnicode(String str) {
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c > 255) {
				sb.append("\\u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
			} else {
				sb.append(c);
			}

		}
		return (new String(sb));
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @param chinese汉字串
	 * @return 汉语拼音首字母字符串
	 *//*
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i],
							defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}
*/
	/**
	 * @Description 在scrollview中放listview时重新设置listview高度
	 * @param listView
	 * @return void
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * @Description 照片选择
	 * @param activity
	 * @param requestCode
	 * @return void
	 */
	public static void choosePhoto(Activity activity, int requestCode) {
		Intent getImage = new Intent();
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/*");
		// if (android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.KITKAT) {
		// getImage.setAction(Intent.ACTION_OPEN_DOCUMENT);
		// } else {
		getImage.setAction(Intent.ACTION_GET_CONTENT);
		// }
		activity.startActivityForResult(getImage, requestCode);
	}

	/**
	 * @Description 照片选择
	 * @param activity
	 * @param requestCode
	 * @return void
	 */
	public static void choosePhoto(Fragment fragment, int requestCode) {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/*");
		fragment.startActivityForResult(getImage, requestCode);
	}

	/**
	 * @Description 调用系统相机照相
	 * @param activity
	 * @param requestCode
	 * @return void
	 */
	public static void takePhoto(Activity activity, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 需裁剪图片的uri地址
	 * 
	 * @param activity
	 * @param uri
	 * @param requestCode
	 * @param x
	 *            输出尺寸x
	 * @param y输出尺寸y
	 */
	public static void croppedPhoto(Activity activity, Uri uri,
			int requestCode, int x, int y) {
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);// 输出图片大小
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, baos);
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (OutOfMemoryError e2) {
			//HhxhLog.e("bitmapToBase64----------OutOfMemoryError");
			baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 50, baos);
			byte[] bitmapBytes = baos.toByteArray();
			result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		Bitmap bitmap = null;
		byte[] bytes = null;
		try {
			bytes = Base64.decode(base64Data, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (OutOfMemoryError e) {// 有时无法catch
			e.printStackTrace();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;// 图片高宽度都为原来的二分之一，即图片大小为原来的大小的四分之一
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
		}
		return bitmap;
	}

	/**
	 * @Description 判断是否为手机号
	 * @param mobiles
	 * @return boolean
	 */
	public static boolean isMobileNO(String mobiles) {
		if ("".equals(mobiles) || null == mobiles)
			return false;
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	private boolean isMobile(String number) {
		boolean res = false;
		// 手机号判断
		String exp = "1[345789]{1}[0-9]{9}";
		Pattern p = Pattern.compile(exp);
		Matcher m = p.matcher(number);
		res = m.matches();
		return res;
	}
	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * @Description 判断是否为平板
	 * @param context
	 * @return boolean
	 */
//	public static boolean isTablet(Context context) {
//		return context.getResources().getBoolean(R.bool.isTablet);
//	}

	/**
	 * Bitmap转Drawable
	 * 
	 * @param bm
	 * @return
	 */
	public static Drawable getBitmap2Drawable(Bitmap bm) {
		BitmapDrawable bd = new BitmapDrawable(bm);
		return bd;
	}

	/**
	 * Drawable转Bitmap
	 * 
	 * @param dr
	 * @return
	 */
	public static Bitmap getDrawable2Bitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 把bitmap转换成byte[]
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBitmap2Byte(Bitmap bitmap) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, output);
			bitmap.recycle();
			byte[] result = output.toByteArray();
			output.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取jid中的id，去掉域名
	 * 
	 * @param jid
	 *            ：im用户的jid
	 */
	public static String getImId(String jid) {
		if (!TextUtils.isEmpty(jid) && jid.indexOf("@") != -1) {
			jid = jid.split("@")[0];
		}
		return jid;
	}



	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return true为空,false不为空
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 根据时间生成图片名称
	 * 
	 * @return
	 */
	public static String getPhotoName() {
		return System.nanoTime() + random.nextInt(25) + ".jpg";
	}



	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 分割字符串
	 * 
	 * @param curString
	 * @param delimiter
	 * @return
	 */
	public static String[] splitString(String curString, String delimiter) {
		if (curString != null && delimiter != null
				&& curString.indexOf(delimiter) != -1) {
			return curString.split(delimiter);
		}
		return null;
	}

	/**
	 * 获取不同尺寸的图片
	 * 
	 * @param photoUrl
	 *            图片原地址
	 * @param size
	 *            图片大小尺寸(现有三种，参数分别为1、2、3，图片从小到大)
	 * @return
	 */
	public static String photoSizeUrl(String photoUrl, int size) {
		StringBuffer sb = new StringBuffer();
		if (photoUrl != null && photoUrl.lastIndexOf(".") != -1) {
			String photoBefore = photoUrl.substring(0,
					photoUrl.lastIndexOf("."));
			String photoAfter = photoUrl.substring(photoUrl.lastIndexOf("."),
					photoUrl.length());
			sb.append(photoBefore);
			if (size == 1) {
				sb.append("@1x");
			} else if (size == 2) {
				sb.append("@2x");
			} else if (size == 3) {
				sb.append("@4x");
			}
			sb.append(photoAfter);
			return sb.toString();
		}
		return photoUrl;

	}

	/**
	 * 获取图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap bitmap) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //API 19
		// return bitmap.getAllocationByteCount();
		// }
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
																			// 12
			return bitmap.getByteCount();
			
		}
		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
	}

	/**
	 * 复制文字到前切板
	 * 
	 * @param context
	 * @param content要复制到剪切板的内容
	 */
	public static void saveToClipboard(Context context, String content) {
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content);
	}


	/**
	 * 获取文件路径中的名称
	 * 
	 * @param pathandname
	 *            路径
	 * @return 名称
	 */
	public static String getFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.length();
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}

	}

	/**
	 * 判断当前是否是聊天界面
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isActivityStatcTop() {
		ActivityManager mActivityManager = (ActivityManager) MyApplication
				.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String className = rti.get(0).topActivity.getClassName();
		if ("com.hhxh.circle.eim.activity.im.ChatActivity".equals(className)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置字体
	 * 
	 * @return
	 */
	public static Typeface settingTypeface(Context contect, String typefacePath) {
		if (!SharedMothed.isEmpty(typefacePath)) {
			AssetManager mgr = contect.getAssets();// 得到AssetManager
			Typeface tf = Typeface.createFromAsset(mgr, typefacePath);// 根据路径得到Typeface
			if (tf != null) {
				return tf;
			}
		}
		return null;

	}
	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}
	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}
	
	public static String getTotalSum(int num,double price){
		return formateRate(String.valueOf(num*price));
	}
	
	//格式化 电子化移交完成率 保留两位
		public static String formateRate(String rateStr){
			 if(rateStr.indexOf(".") != -1){
				 //获取小数点的位置
	   	   	  	 int num = 0;
	   	   	  	 num = rateStr.indexOf(".");
	   	   	  	 
	   	   	  	 //获取小数点后面的数字 是否有两位 不足两位补足两位
	   	   	  	 String dianAfter = rateStr.substring(0,num+1);
	   	   	  	 String afterData = rateStr.replace(dianAfter, "");
	   	   	  	 if(afterData.length() < 2){
	   	   	  	    afterData = afterData + "0" ;
	   	   	  	 }else{
	   	   	  		 afterData = afterData;
	   	   	  	 }
	   	   	  	 return rateStr.substring(0,num) + "." + afterData.substring(0,2);
	   	   	  }else{
	   	   	  	if(rateStr == "1"){
	   	   	  	   return "100";
	   	   	  	}else{
	   	   	  	 	return rateStr;
	   	   	  	}
	   	   	  }
		}
		public static boolean isInstallApp(Context mContext, String packageName) {
			       final PackageManager packageManager = mContext.getPackageManager();  
			       List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);  
			      List<String> pName = new ArrayList<String>();  
			       if (pinfo != null) {  
			           for (int i = 0; i < pinfo.size(); i++) {  
			               String pn = pinfo.get(i).packageName;  
			                pName.add(pn);  
			           }  
			       }  
			        return pName.contains(packageName);  
		   }



}
